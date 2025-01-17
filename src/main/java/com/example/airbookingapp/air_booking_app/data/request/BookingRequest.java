package com.example.airbookingapp.air_booking_app.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    @NotNull(message = "Flight ID cannot be null")
    private String flightId;

    @NotNull(message = "Seat ID cannot be null")
    private Integer seatId;
}
