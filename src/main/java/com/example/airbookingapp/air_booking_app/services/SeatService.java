package com.example.airbookingapp.air_booking_app.services;

import com.example.airbookingapp.air_booking_app.data.request.SeatRequest;
import com.example.airbookingapp.air_booking_app.data.response.SeatResponse;

import java.util.List;

public interface SeatService {
    SeatResponse getSeatDetails(String flightId, Integer seatId);

    List<SeatResponse> getAvailableSeats(String flightId);

    SeatResponse addSeat(String flightId, SeatRequest seatRequest);

    SeatResponse updateSeat(String flightId, Integer seatId, SeatRequest seatRequest);

    void deleteSeat(String flightId, Integer seatId);
}
