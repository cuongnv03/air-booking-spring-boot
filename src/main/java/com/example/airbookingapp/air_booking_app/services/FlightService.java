package com.example.airbookingapp.air_booking_app.services;

import com.example.airbookingapp.air_booking_app.dto.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.dto.request.SearchFlightRequest;
import com.example.airbookingapp.air_booking_app.dto.response.FlightResponse;
import com.example.airbookingapp.air_booking_app.dto.response.PageResponse;

import java.util.List;

public interface FlightService {

    FlightResponse createFlight(FlightRequest request);

    FlightResponse getFlightById(String id);

    PageResponse<FlightResponse> getAllFlights(int page, int size);

    void deleteFlight(String flightId);

    FlightResponse updateFlight(FlightRequest flightRequest, String flightId);

    PageResponse<FlightResponse> searchFlights(List<SearchFlightRequest> filters, int page, int size);
}
