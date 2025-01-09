package com.example.airbookingapp.air_booking_app.web;

import com.example.airbookingapp.air_booking_app.dto.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.dto.request.SearchFlightRequest;
import com.example.airbookingapp.air_booking_app.dto.response.FlightResponse;
import com.example.airbookingapp.air_booking_app.dto.response.PageResponse;
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
        return flightService.createFlight(flightRequest);
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
    public PageResponse<FlightResponse> getAllFlights(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                      @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return flightService.getAllFlights(page, size);
    }

    // Search flights
    @GetMapping("/user/flights/search")
    @PreAuthorize("hasRole('USER')")
    public PageResponse<FlightResponse> searchFlights(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                      @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                      @Valid @RequestBody List<SearchFlightRequest> searchFlightRequests) {
        return flightService.searchFlights(searchFlightRequests, page, size);
    }
}
