package com.example.airbookingapp.air_booking_app.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponse {
    private String flightId;
    private String company; // Airline company name
    private String departurePoint;
    private String destinationPoint;
    private LocalDateTime scheduledTime;
    private Long priceE; // Economy class price
    private Long priceB; // Business class price
    private Integer baggageAllowanceE; // Economy baggage allowance
    private Integer baggageAllowanceB; // Business baggage allowance
    private Integer numSeatsE; // Economy seats available
    private Integer numSeatsB; // Business seats available
}
