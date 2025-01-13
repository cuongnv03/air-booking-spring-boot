package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.data.mapper.FlightMapper;
import com.example.airbookingapp.air_booking_app.data.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.data.request.SearchFlightRequest;
import com.example.airbookingapp.air_booking_app.data.response.FlightResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Flight;
import com.example.airbookingapp.air_booking_app.repositories.FlightRepository;
import com.example.airbookingapp.air_booking_app.services.FlightService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<Page<FlightResponse>> getAllFlights(int sizePerPage) {
        List<Flight> flights = flightRepository.findAll(); // Fetch POJOs from repository
        List<FlightResponse> listFlights = flights.stream().map(flightMapper::fromPojoToResponse).toList(); // Map POJOs to Response DTOs
        return getPages(sizePerPage, listFlights);
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
    public List<Page<FlightResponse>> searchFlights(List<SearchFlightRequest> filters, int sizePerPage) {
        List<Flight> flights = flightRepository.search(filters); // Custom jOOQ implementation for search
        List<FlightResponse> listFlights = flights.stream().map(flightMapper::fromPojoToResponse).toList(); // Map POJOs to Response DTOs
        return getPages(sizePerPage, listFlights);
    }

    private List<Page<FlightResponse>> getPages(int sizePerPage, List<FlightResponse> listFlights) {
        List<Page<FlightResponse>> result = new ArrayList<>();
        int totalFlights = listFlights.size();
        int totalPages = (int) Math.ceil((double) totalFlights / sizePerPage);
        for (int i = 0; i < totalPages; i++) {
            int start = i * sizePerPage;
            int end = Math.min(start + sizePerPage, totalFlights);

            List<FlightResponse> pageContent = listFlights.subList(start, end);
            Page<FlightResponse> page = new PageImpl<>(pageContent, PageRequest.of(i, sizePerPage), totalFlights);
            result.add(page);
        }
        return result;
    }
}
