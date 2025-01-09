package com.example.airbookingapp.air_booking_app.controllers;

import com.example.airbookingapp.air_booking_app.data.request.BookingRequest;
import com.example.airbookingapp.air_booking_app.data.response.BookingResponse;
import com.example.airbookingapp.air_booking_app.services.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Create a new booking
    @PostMapping("/user/bookings/create")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        BookingResponse bookingResponse = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(bookingResponse);
    }

    // Get all bookings for the authenticated user
    @GetMapping("/user/bookings")
    public ResponseEntity<List<BookingResponse>> getUserBookings() {
        List<BookingResponse> bookings = bookingService.getAllBookingsByUser();
        return ResponseEntity.ok(bookings);
    }

    // Check if a seat is already booked
    @GetMapping("/user/seats/{seatId}/status")
    public ResponseEntity<Boolean> isSeatBooked(@PathVariable String seatId) {
        boolean isBooked = bookingService.isSeatBooked(seatId);
        return ResponseEntity.ok(isBooked);
    }
}
