package com.example.airbookingapp.air_booking_app.services;

import com.example.airbookingapp.air_booking_app.data.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.data.request.SearchFlightRequest;
import com.example.airbookingapp.air_booking_app.data.response.FlightResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FlightService {

    FlightResponse createFlight(FlightRequest request);

    FlightResponse getFlightById(String id);

    Page<FlightResponse> getAllFlights(int page, int size);

    void deleteFlight(String flightId);

    FlightResponse updateFlight(FlightRequest flightRequest, String flightId);

    Page<FlightResponse> searchFlights(List<SearchFlightRequest> filters, int page, int size);
}
