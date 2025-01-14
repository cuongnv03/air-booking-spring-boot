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

    // Thêm một ghế mới
    @PostMapping("/admin/flights/{flightId}/seats")
    public ResponseEntity<SeatResponse> addSeat(@PathVariable String flightId,
                                                @Valid @RequestBody SeatRequest seatRequest) {
        SeatResponse response = seatService.addSeat(flightId, seatRequest);
        return ResponseEntity.ok(response);
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
