// File will be removed in the future
// This file is kept as a reference for the search feature of the application
package com.example.airbookingapp.air_booking_app.services.search;

import com.example.airbookingapp.air_booking_app.entity.Flight;
import com.example.airbookingapp.air_booking_app.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlightServices {

    @Autowired
    private FlightRepository flightRepository;

    // Add a flight
    public void addFlight(Flight flight) {
        flightRepository.save(flight);
    }

    // Update flight time
    public Flight updateFlightTime(Flight updatedFlight, String flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with ID: " + flightId));
        Optional.ofNullable(updatedFlight.getDepartureTime()).ifPresent(flight::setDepartureTime);
        Optional.ofNullable(updatedFlight.getReturnTime()).ifPresent(flight::setReturnTime);
        return flightRepository.save(flight);
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
    public List<Page<Flight>> getAllFlights(int sizePerPage) {
        List<Flight> allFlights = flightRepository.findAll();
        return getPages(sizePerPage, allFlights);
    }

    public List<Page<Flight>> searchFlights(List<SearchCriteria> searchCriteria, int sizePerPage) {
        Specification<Flight> specification = Specification.where(null);

        // Duyệt qua danh sách các điều kiện và kết hợp chúng
        specification = searchCriteria.stream()
                .map(FlightSpecifications::buildSpecification) // Chuyển từng SearchCriteria thành Specification
                .reduce(Specification::and) // Kết hợp tất cả Specification
                .orElse(specification); // Nếu không có điều kiện nào, trả về Specification rỗng
        List<Flight> searchResult = flightRepository.findAll(specification);
        return getPages(sizePerPage, searchResult);
    }

    private List<Page<Flight>> getPages(int sizePerPage, List<Flight> listFlights) {
        List<Page<Flight>> result = new ArrayList<>();
        int totalFlights = listFlights.size();
        int totalPages = (int) Math.ceil((double) totalFlights / sizePerPage);
        for (int i = 0; i < totalPages; i++) {
            int start = i * sizePerPage;
            int end = Math.min(start + sizePerPage, totalFlights);

            List<Flight> pageContent = listFlights.subList(start, end);
            Page<Flight> page = new PageImpl<>(pageContent, PageRequest.of(i, sizePerPage), totalFlights);
            result.add(page);
        }
        return result;
    }
}