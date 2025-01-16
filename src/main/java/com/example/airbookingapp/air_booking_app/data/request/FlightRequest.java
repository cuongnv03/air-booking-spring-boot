package com.example.airbookingapp.air_booking_app.data.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightRequest {
    private String company; // Airline company name
    private String departurePoint;
    private String destinationPoint;
    private LocalDateTime scheduledTime;
    private Long priceE; // Economy class price
    private Long priceB; // Business class price
    private Integer baggageAllowanceE; // Economy baggage allowance
    private Integer baggageAllowanceB; // Business baggage allowance
}
