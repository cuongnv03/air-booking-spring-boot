package com.example.airbookingapp.air_booking_app.data.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightResponse {
    private String flightId;
    private String company; // Airline company name
    private String departurePoint;
    private String destinationPoint;
    private LocalDateTime scheduledTime;
    private long priceE; // Economy class price
    private long priceB; // Business class price
    private int baggageAllowanceE; // Economy baggage allowance
    private int baggageAllowanceB; // Business baggage allowance
    private int numSeatsE; // Economy seats available
    private int numSeatsB; // Business seats available
}
