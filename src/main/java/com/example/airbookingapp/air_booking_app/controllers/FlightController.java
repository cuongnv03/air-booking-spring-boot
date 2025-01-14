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

    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    // Thêm mới chuyến bay
    @PostMapping("/admin/flights")
    public ResponseEntity<FlightResponse> addFlight(@Valid @RequestBody FlightRequest flightRequest) {
        FlightResponse response = flightService.createFlight(flightRequest);
        return ResponseEntity.ok(response);
    }

    // Cập nhật thông tin chuyến bay
    @PutMapping("/admin/flights/{flightId}")
    public ResponseEntity<FlightResponse> updateFlight(@PathVariable String flightId, @Valid @RequestBody FlightRequest flightRequest) {
        FlightResponse response = flightService.updateFlight(flightRequest, flightId);
        return ResponseEntity.ok(response);
    }

    // Xóa chuyến bay
    @DeleteMapping("/admin/flights/{flightId}")
    public ResponseEntity<String> deleteFlight(@PathVariable String flightId) {
        flightService.deleteFlight(flightId);
        return ResponseEntity.ok("Đã xóa chuyến bay với flightId: " + flightId);
    }

    // Lấy danh sách tất cả các chuyến bay
    @GetMapping("/flights")
    public ResponseEntity<Page<FlightResponse>> getAllFlights(@RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size){
        Page<FlightResponse> response = flightService.getAllFlights(page, size);
        return ResponseEntity.ok(response);
    }

    // Tìm kiếm chuyến bay theo tiêu chí
    @PostMapping("/flights/search")
    public ResponseEntity<Page<FlightResponse>> searchFlights(
            @RequestBody List<SearchFlightRequest> searchFlightRequests,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        Page<FlightResponse> response = flightService.searchFlights(searchFlightRequests, page, size);
        return ResponseEntity.ok(response);
    }

    // Lấy thông tin chi tiết của chuyến bay
    @GetMapping("/flights/{flightId}")
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable String flightId) {
        FlightResponse response = flightService.getFlightById(flightId);
        return ResponseEntity.ok(response);
    }
}
