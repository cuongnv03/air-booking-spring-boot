package com.example.airbookingapp.air_booking_app.controllers;

import com.example.airbookingapp.air_booking_app.data.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.data.request.SearchFlightRequest;
import com.example.airbookingapp.air_booking_app.data.response.FlightResponse;
import com.example.airbookingapp.air_booking_app.data.response.PaginatedResponse;
import com.example.airbookingapp.air_booking_app.services.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

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
    public ResponseEntity<PaginatedResponse<FlightResponse>> getFlights(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                        @RequestParam(value = "size", defaultValue = "10") int size,
                                                                        @RequestParam(required = false) List<String> filters){
        if (filters == null || filters.isEmpty()) {
            // Call getAllFlights if no filters are provided
            PaginatedResponse<FlightResponse> response = flightService.getAllFlights(page, size);
            return ResponseEntity.ok(response);
        } else {
            // Parse filters into SearchFlightRequest and call searchFlights
            List<SearchFlightRequest> searchRequests = parseFilters(filters);
            PaginatedResponse<FlightResponse> response = flightService.searchFlights(searchRequests, page, size);
            return ResponseEntity.ok(response);
        }
    }

    // Lấy thông tin chi tiết của chuyến bay
    @GetMapping("/flights/{flightId}")
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable String flightId) {
        FlightResponse response = flightService.getFlightById(flightId);
        return ResponseEntity.ok(response);
    }

    // Helper method to parse query parameters into SearchFlightRequest objects
    private List<SearchFlightRequest> parseFilters(List<String> filters) {
        return filters.stream()
                .map(filter -> {
                    String[] parts = filter.split(":");
                    if (parts.length != 3) {
                        throw new IllegalArgumentException("Invalid filter format. Expected format: key:operator:value");
                    }
                    SearchFlightRequest request = new SearchFlightRequest();
                    request.setKey(parts[0]);
                    request.setOperator(parts[1]);
                    request.setValue(parts[2]);
                    return request;
                })
                .collect(Collectors.toList());
    }
}
