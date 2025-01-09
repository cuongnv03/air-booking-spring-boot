package com.example.airbookingapp.air_booking_app.data.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Data
public class FlightResponse {
    private String flightId;
    private String airline;
    private String departurePoint;
    private String destinationPoint;
    private LocalDateTime departureTime;
    private LocalDateTime returnTime;
    private LocalTime duration;
}
