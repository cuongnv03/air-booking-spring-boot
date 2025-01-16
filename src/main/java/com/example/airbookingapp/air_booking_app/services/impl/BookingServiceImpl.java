package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.data.mapper.BookingMapper;
import com.example.airbookingapp.air_booking_app.data.request.BookingRequest;
import com.example.airbookingapp.air_booking_app.data.request.PaymentRequest;
import com.example.airbookingapp.air_booking_app.data.response.BookingResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Booking;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Flight;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Seat;
import com.example.airbookingapp.air_booking_app.repositories.BookingRepository;
import com.example.airbookingapp.air_booking_app.repositories.CardRepository;
import com.example.airbookingapp.air_booking_app.repositories.FlightRepository;
import com.example.airbookingapp.air_booking_app.repositories.SeatRepository;
import com.example.airbookingapp.air_booking_app.security.services.UserDetailsImpl;
import com.example.airbookingapp.air_booking_app.services.BookingService;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;
    private final CardRepository cardRepository;
    private final BookingMapper bookingMapper;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              SeatRepository seatRepository,
                              FlightRepository flightRepository, CardRepository cardRepository,
                              BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.flightRepository = flightRepository;
        this.cardRepository = cardRepository;
        this.bookingMapper = bookingMapper;
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
        Booking savedBooking = bookingRepository.save(booking);
        // Convert to response DTO
        return bookingMapper.fromPojoToResponse(savedBooking);
    }

    @Override
    public BookingResponse getBookingById(String bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if (booking == null) {
            throw new RuntimeException("Booking ID " + bookingId + " not found.");
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

    // Xóa một booking theo ID
    @Override
    public void deleteBooking(String bookingId) {
        boolean deleted = bookingRepository.deleteById(bookingId);
        if (!deleted) {
            throw new RuntimeException("Không thể xóa booking với ID: " + bookingId);
        }
    }

    // Cập nhật trạng thái thanh toán của booking
    @Override
    public void updatePaymentStatus(String bookingId, boolean paymentStatus) {
        int rowsAffected = bookingRepository.updatePaymentStatus(bookingId, paymentStatus);
        if (rowsAffected == 0) {
            throw new RuntimeException("Không thể cập nhật trạng thái thanh toán cho booking với ID: " + bookingId);
        }
    }

    @Override
    public Long getBookedSeatPrice(String bookingId) {
        Long price = bookingRepository.getBookedSeatPrice(bookingId);
        if (price == null) {
            throw new ResourceNotFoundException("Booking or Seat not found with ID: " + bookingId);
        }
        return price;
    }

    @Override
    @Transactional
    public void payBooking(String bookingId, PaymentRequest paymentRequest) {
        // Retrieve authenticated user's details
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        // Step 1: Fetch booking details
        var booking = bookingRepository.findByBookingId(bookingId);
        if (booking == null) {
            throw new ResourceNotFoundException("Booking not found with ID: " + bookingId);
        }

        if (booking.getPaymentStatus()) {
            throw new IllegalStateException("Booking has already been paid.");
        }

        // Step 2: Fetch seat price
        Long seatPrice = bookingRepository.getBookedSeatPrice(booking.getBookingId());
        if (seatPrice == null) {
            throw new ResourceNotFoundException("Seat price not found for booking ID: " + bookingId);
        }

        // Step 3: Validate card details
        var card = cardRepository.findCardByDetails(
                userDetails.getUserId(),
                paymentRequest.getCardNumber(),
                paymentRequest.getCardholderName(),
                paymentRequest.getCvvCode()
        );
        if (card == null) {
            throw new ResourceNotFoundException("Invalid card details.");
        }

        // Step 4: Check balance
        if (card.getBalanceAmount().compareTo(BigDecimal.valueOf(seatPrice)) < 0) {
            throw new IllegalStateException("Insufficient balance.");
        }

        // Step 5: Deduct balance and update booking
        cardRepository.deductBalance(card.getCardNumber(), seatPrice);
        bookingRepository.updatePaymentStatus(booking.getBookingId(), true);
    }
}
