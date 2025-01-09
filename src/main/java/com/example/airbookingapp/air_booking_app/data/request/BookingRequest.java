package com.example.airbookingapp.air_booking_app.data.request;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

@Data
public class BookingRequest {
    @NotEmpty(message = "Flight ID cannot be empty")
    private String flightId;

    @NotEmpty(message = "Seat ID cannot be empty")
    private String seatId;
}