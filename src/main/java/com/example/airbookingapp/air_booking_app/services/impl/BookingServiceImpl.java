package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.data.mapper.BookingMapper;
import com.example.airbookingapp.air_booking_app.data.request.BookingRequest;
import com.example.airbookingapp.air_booking_app.data.request.PaymentRequest;
import com.example.airbookingapp.air_booking_app.data.response.BookingResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Booking;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Flight;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Seat;
import com.example.airbookingapp.air_booking_app.repositories.BookingRepository;
import com.example.airbookingapp.air_booking_app.repositories.FlightRepository;
import com.example.airbookingapp.air_booking_app.repositories.SeatRepository;
import com.example.airbookingapp.air_booking_app.security.services.UserDetailsImpl;
import com.example.airbookingapp.air_booking_app.services.BookingService;
import com.example.airbookingapp.air_booking_app.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;
    private final PaymentService paymentService;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponse getBookingById(String bookingId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if (booking == null) {
            throw new RuntimeException("Booking ID " + bookingId + " not found.");
        }
        if (!booking.getUserId().equals(userDetails.getUserId())) {
            throw new RuntimeException("Booking ID " + bookingId + " does not belong to user ID " + userDetails.getUserId());
        }
        return bookingMapper.fromPojoToResponse(booking);
    }

    @Override
    public List<BookingResponse> getAllBookingsByUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        List<Booking> bookings = bookingRepository.findAllByUserId(userDetails.getUserId());
        return bookings.stream()
                .map(bookingMapper::fromPojoToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        // Retrieve authenticated user's details
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        // Validate flight
        Flight flight = flightRepository.findByFlightId(bookingRequest.getFlightId());
        if (flight == null) {
            throw new RuntimeException("Flight ID " + bookingRequest.getFlightId() + " is invalid.");
        }
        // Validate seat
        Seat seat = seatRepository.findByFlightIdAndSeatId(bookingRequest.getFlightId(), bookingRequest.getSeatId());
        if (seat == null || seat.getSeatStatus()) {
            throw new RuntimeException("Seat ID " + bookingRequest.getSeatId() + " is not available.");
        }
        // Update seat status to "BOOKED"
        seatRepository.updateSeatStatus(bookingRequest.getFlightId(), bookingRequest.getSeatId(), true);
        // Create booking record
        Booking booking = bookingMapper.fromRequestToPojo(bookingRequest, userDetails.getUserId());
        booking.setAmountPayable(seat.getPrice());
        Booking savedBooking = bookingRepository.save(booking);
        // Convert to response DTO
        return bookingMapper.fromPojoToResponse(savedBooking);
    }

    @Override
    public BookingResponse changeBooking(String oldBookingId, BookingRequest newBookingRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // Validate old booking
        Booking oldBooking = bookingRepository.findByBookingId(oldBookingId);
        if (oldBooking == null || !oldBooking.getUserId().equals(userDetails.getUserId())) {
            throw new RuntimeException("Unauthorized access to booking ID: " + oldBookingId);
        }
        // Validate new flight and seat
        Flight newFlight = flightRepository.findByFlightId(newBookingRequest.getFlightId());
        if (newFlight == null) {
            throw new RuntimeException("Flight ID " + newBookingRequest.getFlightId() + " is invalid.");
        }

        Seat newSeat = seatRepository.findByFlightIdAndSeatId(newBookingRequest.getFlightId(), newBookingRequest.getSeatId());
        if (newSeat == null || newSeat.getSeatStatus()) {
            throw new RuntimeException("Seat ID " + newBookingRequest.getSeatId() + " is not available.");
        }

        if (newSeat.equals(seatRepository.findByFlightIdAndSeatId(oldBooking.getFlightId(), oldBooking.getSeatId()))){
            throw new RuntimeException("New seat is the same as old seat.");
        }

        // Create new booking
        seatRepository.updateSeatStatus(newBookingRequest.getFlightId(), newBookingRequest.getSeatId(), true);
        Booking newBooking = bookingMapper.fromRequestToPojo(newBookingRequest, userDetails.getUserId());

        if (oldBooking.getPaymentStatus()){
            newBooking.setAmountPayable(newSeat.getPrice() - bookingRepository.getBookedSeatPrice(oldBookingId));
            if (newBooking.getAmountPayable() == 0) {
                newBooking.setPaymentStatus(true);
            }
        } else {
            newBooking.setAmountPayable(newSeat.getPrice());
        }
        Booking savedNewBooking = bookingRepository.save(newBooking);
        // Cancel old booking
        seatRepository.updateSeatStatus(oldBooking.getFlightId(), oldBooking.getSeatId(), false);
        bookingRepository.deleteById(oldBooking.getBookingId());
        return bookingMapper.fromPojoToResponse(savedNewBooking);
    }

    // Xóa một booking theo ID
    @Override
    public void cancelBooking(String bookingId, PaymentRequest paymentRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // Validate booking
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if (booking == null || !booking.getUserId().equals(userDetails.getUserId())) {
            throw new RuntimeException("Unauthorized access to booking ID: " + bookingId);
        }

        if (booking.getPaymentStatus()) {
            paymentService.processPayment(bookingId, paymentRequest, PaymentService.PaymentAction.REFUND);
        }

        // Cancel booking
        seatRepository.updateSeatStatus(booking.getFlightId(), booking.getSeatId(), false);
        bookingRepository.deleteById(booking.getBookingId());
    }
}
