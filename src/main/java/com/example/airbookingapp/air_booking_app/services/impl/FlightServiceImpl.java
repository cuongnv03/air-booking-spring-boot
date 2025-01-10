package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.data.mapper.FlightMapper;
import com.example.airbookingapp.air_booking_app.data.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.data.request.SearchFlightRequest;
import com.example.airbookingapp.air_booking_app.data.response.FlightResponse;
import com.example.airbookingapp.air_booking_app.data.response.PageResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Flight;
import com.example.airbookingapp.air_booking_app.repositories.FlightRepository;
import com.example.airbookingapp.air_booking_app.services.FlightService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {
    private final FlightMapper flightMapper;
    private final FlightRepository flightRepository;
    public FlightServiceImpl(FlightMapper flightMapper, FlightRepository flightRepository) {
        this.flightMapper = flightMapper;
        this.flightRepository = flightRepository;
    }

    @Override
    public FlightResponse createFlight(FlightRequest request) {
        Flight flight = flightMapper.fromRequestToPojo(request); // Map DTO to POJO
        Flight savedFlight = flightRepository.save(flight);      // Save POJO via repository
        return flightMapper.fromPojoToResponse(savedFlight);     // Map POJO to Response DTO
    }

    @Override
    public FlightResponse getFlightById(String id) {
        Flight flight = flightRepository.findById(id);           // Fetch POJO from repository
        return flightMapper.fromPojoToResponse(flight);          // Map POJO to Response DTO
    }

    @Override
    public PageResponse<FlightResponse> getAllFlights(int page, int size) {
        List<Flight> flights = flightRepository.findAll(page, size); // Fetch POJOs from repository
        long totalElements = flightRepository.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return PageResponse.<FlightResponse>builder()
                .currentPage(page)
                .totalPages(totalPages)
                .pageSize(size)
                .totalElements(totalElements)
                .data(flights.stream().map(flightMapper::fromPojoToResponse).toList()) // Map POJOs to Response DTOs
                .build();
    }

    @Override
    public void deleteFlight(String flightId) {
        if (!flightRepository.deleteById(flightId)) {
            throw new RuntimeException("Flight ID " + flightId + " is not found.");
        }
    }

    @Override
    public FlightResponse updateFlight(FlightRequest flightRequest, String flightId) {
        Flight existingFlight = flightRepository.findById(flightId);
        if (existingFlight == null) {
            throw new RuntimeException("Flight ID " + flightId + " is not found.");
        }
        // Use MapStruct to update existingFlight with values from flightRequest
        flightMapper.updateFromRequestToPojo(flightRequest, existingFlight);
        // Ensure the flightId remains unchanged
        existingFlight.setFlightId(flightId);
        // Persist changes
        Flight updatedFlight = flightRepository.update(flightId, existingFlight);
        // Convert updatedFlight to Response DTO
        return flightMapper.fromPojoToResponse(updatedFlight);
    }

    @Override
    public PageResponse<FlightResponse> searchFlights(List<SearchFlightRequest> filters, int page, int size) {
        List<Flight> flights = flightRepository.search(filters, page, size); // Custom jOOQ implementation for search
        long totalElements = flightRepository.countSearch(filters); // Count total matching records
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return PageResponse.<FlightResponse>builder()
                .currentPage(page)
                .totalPages(totalPages)
                .pageSize(size)
                .totalElements(totalElements)
                .data(flights.stream().map(flightMapper::fromPojoToResponse).toList()) // Map POJOs to Response DTOs
                .build();
    }
}
