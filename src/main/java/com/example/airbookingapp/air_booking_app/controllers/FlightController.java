package com.example.airbookingapp.air_booking_app.controllers;

import com.example.airbookingapp.air_booking_app.data.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.data.request.SearchFlightRequest;
import com.example.airbookingapp.air_booking_app.data.response.FlightResponse;
import com.example.airbookingapp.air_booking_app.services.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping
public class FlightController {

    @Autowired
    private final FlightService flightService;
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    // Add a flight
    @PostMapping("/admin/flights/add")
    public FlightResponse addFlight(@Valid @RequestBody FlightRequest flightRequest) {
        return flightService.createFlight(flightRequest);
    }

    // Update flight time
    @PutMapping("/admin/flights/update-time/{flightId}")
    public FlightResponse updateFlightTime(@Valid @RequestBody FlightRequest flightRequest, @PathVariable String flightId) {
        return flightService.updateFlight(flightRequest, flightId);
    }

    // Delete a flight
    @DeleteMapping("/admin/flights/delete/{flightId}")
    public ResponseEntity<String> deleteFlight(@PathVariable String flightId) {
        flightService.deleteFlight(flightId);
        return ResponseEntity.ok("Flight " + flightId + " deleted successfully");
    }

    // Get all flights
    @GetMapping("/user/flights")
    public List<Page<FlightResponse>> getAllFlights(@RequestParam(value = "sizePerPage", required = false, defaultValue = "10") int sizePerPage) {
        return flightService.getAllFlights(sizePerPage);
    }

    // Search flights
    @GetMapping("/user/flights/search")
    public List<Page<FlightResponse>> searchFlights(@RequestParam(value = "sizePerPage", required = false, defaultValue = "10") int sizePerPage,
                                                      @Valid @RequestBody List<SearchFlightRequest> searchFlightRequests) {
        return flightService.searchFlights(searchFlightRequests, sizePerPage);
    }
}
