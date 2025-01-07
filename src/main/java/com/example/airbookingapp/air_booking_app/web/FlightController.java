package com.example.airbookingapp.air_booking_app.web;

import com.example.airbookingapp.air_booking_app.dto.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.dto.response.FlightResponse;
import com.example.airbookingapp.air_booking_app.services.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public FlightResponse addFlight(@Valid @RequestBody FlightRequest flightRequest) {
        return flightService.saveFlight(flightRequest);
    }

    // Update flight time
    @PutMapping("/admin/flights/update-time/{flightId}")
    @PreAuthorize("hasRole('ADMIN')")
    public FlightResponse updateFlightTime(@Valid @RequestBody FlightRequest flightRequest, @PathVariable String flightId) {
        return flightService.updateFlight(flightRequest, flightId);
    }

    // Delete a flight
    @DeleteMapping("/admin/flights/delete/{flightId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteFlight(@PathVariable String flightId) {
        flightService.deleteFlight(flightId);
        return ResponseEntity.ok("Flight " + flightId + " deleted successfully");
    }

    // Get all flights (for verification)
    @GetMapping("/admin/flights")
    @PreAuthorize("hasRole('ADMIN')")
    public List<FlightResponse> getAllFlights() {
        return flightService.getAllFlights();
    }
}
