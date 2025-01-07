package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.dto.mapper.FlightMapper;
import com.example.airbookingapp.air_booking_app.dto.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.dto.response.FlightResponse;
import com.example.airbookingapp.air_booking_app.entity.Flight;
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
    public List<FlightResponse> getAllFlights() {
        return flightRepository.findAll().stream()
                .map(flightMapper::fromEntityToResponse).toList();
    }

    @Override
    public FlightResponse findByFlightId(String flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight ID " + flightId + " is not found."));
        return flightMapper.fromEntityToResponse(flight);
    }

    @Override
    public void deleteFlight(String flightId) {
        flightRepository.deleteById(flightId);
    }

    @Override
    public FlightResponse saveFlight(FlightRequest flightRequest) {
        Flight flight = flightMapper.MAPPER.fromRequestToEntity(flightRequest);
        flightRepository.save(flight);
        return flightMapper.MAPPER.fromEntityToResponse(flight);
    }

    @Override
    public FlightResponse updateFlight(FlightRequest flightRequest, String flightId) {
        FlightResponse checkExistingFlight = findByFlightId(flightId);
        if (checkExistingFlight != null) {
            Flight flight = flightMapper.MAPPER.fromRequestToEntity(flightRequest);
            flight.setFlightId(flightId);
            flightRepository.save(flight);
            return flightMapper.MAPPER.fromEntityToResponse(flight);
        } else {
            throw new RuntimeException("Flight ID " + flightId + " is not found.");
        }
    }
}
