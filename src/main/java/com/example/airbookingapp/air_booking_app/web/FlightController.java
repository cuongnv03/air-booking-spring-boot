package com.example.airbookingapp.air_booking_app.web;

import com.example.airbookingapp.air_booking_app.domain.Flight;
import com.example.airbookingapp.air_booking_app.services.flight.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping
public class FlightController {

    @Autowired
    private FlightService flightService;
    // Add a flight
    @PostMapping("/admin/flights/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addFlight(@Valid @RequestBody Flight flight) {
        flightService.addFlight(flight);
        return new ResponseEntity<Flight>(flight, HttpStatus.CREATED);
    }

    // Update flight time
    @PutMapping("/admin/flights/update-time/{flightId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateFlightTime(@Valid @RequestBody Flight flight, @PathVariable String flightId) {
        flightService.updateFlightTime(flight, flightId);
        return new ResponseEntity<Flight>(flight, HttpStatus.OK);
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
    public ResponseEntity<List<Flight>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/user/flights/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Flight>> searchFlights(@Valid @RequestBody Flight searchCriteria) {
        return ResponseEntity.ok(flightService.searchFlights(searchCriteria));
    }

}
