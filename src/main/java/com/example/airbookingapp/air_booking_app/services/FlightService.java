package com.example.airbookingapp.air_booking_app.services;

import com.example.airbookingapp.air_booking_app.dto.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.dto.response.FlightResponse;

import java.util.List;

public interface FlightService {
    List<FlightResponse> getAllFlights();
    FlightResponse findByFlightId(String flightId);
    void deleteFlight(String flightId);
    FlightResponse saveFlight(FlightRequest flightRequest);
    FlightResponse updateFlight(FlightRequest flightRequest, String flightId);
}
