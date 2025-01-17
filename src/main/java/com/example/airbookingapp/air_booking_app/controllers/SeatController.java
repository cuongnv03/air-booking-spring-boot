package com.example.airbookingapp.air_booking_app.controllers;

import com.example.airbookingapp.air_booking_app.data.request.AddSeatsRequest;
import com.example.airbookingapp.air_booking_app.data.request.SeatRequest;
import com.example.airbookingapp.air_booking_app.data.response.SeatResponse;
import com.example.airbookingapp.air_booking_app.services.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    // Lấy thông tin ghế theo flightId và seatId
    @GetMapping("/flights/{flightId}/{seatId}")
    public ResponseEntity<SeatResponse> getSeatDetails(
            @PathVariable String flightId,
            @PathVariable Integer seatId) {
        SeatResponse response = seatService.getSeatDetails(flightId, seatId);
        return ResponseEntity.ok(response);
    }

    // Lấy danh sách ghế trống của một chuyến bay
    @GetMapping("/flights/{flightId}/seats")
    public ResponseEntity<List<SeatResponse>> getAvailableSeats(@PathVariable String flightId) {
        List<SeatResponse> response = seatService.getAvailableSeats(flightId);
        return ResponseEntity.ok(response);
    }

    // Thêm nhiều ghế mới
    @PostMapping("/admin/flights/{flightId}/seats")
    public ResponseEntity<String> addSeats(@PathVariable String flightId,
                                           @Valid @RequestBody AddSeatsRequest addSeatsRequest) {
        try {
            seatService.addSeatsToFlight(flightId, addSeatsRequest);
            return ResponseEntity.ok("Seats added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding seats: " + e.getMessage());
        }
    }

    // Cập nhật thông tin ghế
    @PutMapping("/admin/flights/{flightId}/{seatId}")
    public ResponseEntity<SeatResponse> updateSeat(
            @PathVariable String flightId,
            @PathVariable Integer seatId,
            @Valid @RequestBody SeatRequest seatRequest) {
        SeatResponse response = seatService.updateSeat(flightId, seatId, seatRequest);
        return ResponseEntity.ok(response);
    }

    // Xóa một ghế
    @DeleteMapping("/admin/flights/{flightId}/{seatId}")
    public ResponseEntity<String> deleteSeat(
            @PathVariable String flightId,
            @PathVariable Integer seatId) {
        seatService.deleteSeat(flightId, seatId);
        return ResponseEntity.ok("Ghế với flightId: " + flightId + " và seatId: " + seatId + " đã được xóa.");
    }
}
