package com.example.airbookingapp.air_booking_app.services.flight;

import com.example.airbookingapp.air_booking_app.domain.Flight;
import com.example.airbookingapp.air_booking_app.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    // Add a flight
    public void addFlight(Flight flight) {
        flightRepository.save(flight);
    }

    // Update flight time
    public void updateFlightTime(Flight updatedFlight, String flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with ID: " + flightId));
        Optional.ofNullable(updatedFlight.getDepartureTime()).ifPresent(flight::setDepartureTime);
        Optional.ofNullable(updatedFlight.getReturnTime()).ifPresent(flight::setReturnTime);
        flightRepository.save(flight);
    }

    // Delete a flight
    public void deleteFlight(String flightId) {
        if (flightRepository.existsById(flightId)) {
            flightRepository.deleteById(flightId);
        } else {
            throw new IllegalArgumentException("Flight not found with ID: " + flightId);
        }
    }

    // Get all flights
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public List<Flight> searchFlights(Flight searchCriteria) {
        Specification<Flight> specification = Specification
                .where(FlightSpecifications.hasDeparturePoint(searchCriteria.getDeparturePoint()))
                .and(FlightSpecifications.hasDestinationPoint(searchCriteria.getDestinationPoint()));

        if (searchCriteria.getDepartureTime() != null) {
            specification = specification.and(FlightSpecifications.hasDepartureTime(searchCriteria.getDepartureTime()));
        }

        if (searchCriteria.getReturnTime() != null) {
            specification = specification.and(FlightSpecifications.hasReturnTime(searchCriteria.getReturnTime()));
        }

        return flightRepository.findAll(specification);
    }

}