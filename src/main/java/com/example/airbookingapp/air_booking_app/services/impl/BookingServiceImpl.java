package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.data.mapper.BookingMapper;
import com.example.airbookingapp.air_booking_app.data.request.BookingRequest;
import com.example.airbookingapp.air_booking_app.data.response.BookingResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Booking;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Flight;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Seat;
import com.example.airbookingapp.air_booking_app.repositories.BookingRepository;
import com.example.airbookingapp.air_booking_app.repositories.FlightRepository;
import com.example.airbookingapp.air_booking_app.repositories.SeatRepository;
import com.example.airbookingapp.air_booking_app.security.services.UserDetailsImpl;
import com.example.airbookingapp.air_booking_app.services.BookingService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;
    private final BookingMapper bookingMapper;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              SeatRepository seatRepository,
                              FlightRepository flightRepository,
                              BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.flightRepository = flightRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        // Retrieve authenticated user's details
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // Validate flight
        Flight flight = flightRepository.findById(bookingRequest.getFlightId());
        if (flight == null) {
            throw new RuntimeException("Flight ID " + bookingRequest.getFlightId() + " is invalid.");
        }

        // Validate seat
        Seat seat = seatRepository.findById(bookingRequest.getSeatId());
        if (seat == null || !"NOT BOOKED".equals(seat.getSeatStatus())) {
            throw new RuntimeException("Seat ID " + bookingRequest.getSeatId() + " is not available.");
        }

        // Update seat status to "BOOKED"
        seatRepository.updateSeatStatus(bookingRequest.getSeatId(), "BOOKED");

        // Create booking record
        Booking booking = bookingMapper.fromRequestToPojo(
                bookingRequest,
                userDetails.getUserId(),
                UUID.randomUUID().toString(),
                "NOT PAID"
        );

        Booking savedBooking = bookingRepository.save(booking);

        // Convert to response DTO
        return bookingMapper.fromPojoToResponse(savedBooking);
    }

    @Override
    public BookingResponse findBookingById(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
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

    @Override
    public boolean isSeatBooked(String seatId) {
        return bookingRepository.existsBySeatId(seatId);
    }
}
