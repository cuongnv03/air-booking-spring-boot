package com.example.airbookingapp.air_booking_app.controllers;

import com.example.airbookingapp.air_booking_app.data.request.SeatRequest;
import com.example.airbookingapp.air_booking_app.data.response.SeatResponse;
import com.example.airbookingapp.air_booking_app.services.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // Fetch seat details by ID
    @GetMapping("/user/seats/{seatId}")
    public ResponseEntity<SeatResponse> getSeatDetails(@PathVariable String seatId) {
        SeatResponse seatResponse = seatService.getSeatDetails(seatId);
        return ResponseEntity.ok(seatResponse);
    }

    // List all available seats for a specific flight
    @GetMapping("/user/flights/{flightId}/available")
    public ResponseEntity<List<SeatResponse>> getAvailableSeats(@PathVariable String flightId) {
        List<SeatResponse> availableSeats = seatService.getAvailableSeatsByFlight(flightId);
        return ResponseEntity.ok(availableSeats);
    }

    // Update seat status (e.g., "BOOKED" or "NOT BOOKED")
    @PutMapping("/admin/seats/{seatId}/status")
    public ResponseEntity<SeatResponse> updateSeatStatus(@PathVariable String seatId,
                                                         @Valid @RequestBody SeatRequest seatRequest) {
        SeatResponse updatedSeat = seatService.updateSeatStatus(seatId, seatRequest.getSeatStatus());
        return ResponseEntity.ok(updatedSeat);
    }

    // Update other seat attributes (e.g., baggage allowance, price)
    @PutMapping("/admin/seats/{seatId}/attributes")
    public ResponseEntity<SeatResponse> updateSeatAttributes(@PathVariable String seatId,
                                                             @Valid @RequestBody SeatRequest seatRequest) {
        SeatResponse updatedSeat = seatService.updateSeatAttributes(
                seatId,
                seatRequest.getBaggageAllowance(),
                seatRequest.getPrice()
        );
        return ResponseEntity.ok(updatedSeat);
    }
}
