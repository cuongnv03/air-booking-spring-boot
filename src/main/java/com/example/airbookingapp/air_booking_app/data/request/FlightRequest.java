package com.example.airbookingapp.air_booking_app.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
