package com.example.airbookingapp.air_booking_app.services;

import com.example.airbookingapp.air_booking_app.data.request.SeatRequest;
import com.example.airbookingapp.air_booking_app.data.response.SeatResponse;

import java.util.List;

public interface SeatService {
    // Fetch seat details by seat ID
    SeatResponse getSeatDetails(String seatId);

    // List all available seats for a flight
    List<SeatResponse> getAvailableSeatsByFlight(String flightId);

    // Update seat status (e.g., "BOOKED", "NOT BOOKED")
    SeatResponse updateSeatStatus(String seatId, String status);

    // Update other seat attributes (e.g., baggage allowance, price)
    SeatResponse updateSeatAttributes(String seatId, SeatRequest seatRequest);
}
