package com.example.airbookingapp.air_booking_app.data.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class FlightRequest {
    private String flightId;
    private String airline;
    private String departurePoint;
    private String destinationPoint;
    private LocalDateTime departureTime;
    private LocalDateTime returnTime;
    private LocalTime duration;
}
