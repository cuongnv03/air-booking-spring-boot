package com.example.airbookingapp.air_booking_app.services;

import com.example.airbookingapp.air_booking_app.dto.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.dto.request.SearchFlightRequest;
import com.example.airbookingapp.air_booking_app.dto.response.FlightResponse;
import com.example.airbookingapp.air_booking_app.dto.response.PageResponse;

import java.util.List;

public interface FlightService {
    PageResponse<FlightResponse> getAllFlights(int page, int size);
    FlightResponse findByFlightId(String flightId);
    void deleteFlight(String flightId);
    FlightResponse saveFlight(FlightRequest flightRequest);
    FlightResponse updateFlight(FlightRequest flightRequest, String flightId);
    PageResponse<FlightResponse> searchFlights(List<SearchFlightRequest> searchFlightRequests, int page, int size);
}
