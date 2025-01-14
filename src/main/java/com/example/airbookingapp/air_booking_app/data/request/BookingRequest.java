package com.example.airbookingapp.air_booking_app.data.request;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class BookingRequest {
    @NotNull(message = "Flight ID cannot be null")
    private String flightId;

    @NotNull(message = "Seat ID cannot be null")
    private Integer seatId;
}
