package com.example.airbookingapp.air_booking_app.services;

import com.example.airbookingapp.air_booking_app.data.request.BookingRequest;
import com.example.airbookingapp.air_booking_app.data.response.BookingResponse;

import java.util.List;

public interface BookingService {
    // Create a new booking
    BookingResponse createBooking(BookingRequest bookingRequest);

    // Find a booking by ID
    BookingResponse findBookingById(String bookingId);

    // Find all bookings by user ID
    List<BookingResponse> getAllBookingsByUser();

    // Check if a booking exists for a seat
    boolean isSeatBooked(String seatId);
}
