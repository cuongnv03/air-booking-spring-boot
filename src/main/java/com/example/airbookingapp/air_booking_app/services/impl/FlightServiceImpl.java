package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.data.mapper.FlightMapper;
import com.example.airbookingapp.air_booking_app.data.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.data.request.SearchFlightRequest;
import com.example.airbookingapp.air_booking_app.data.response.FlightResponse;
import com.example.airbookingapp.air_booking_app.data.response.PaginatedResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Flight;
import com.example.airbookingapp.air_booking_app.repositories.FlightRepository;
import com.example.airbookingapp.air_booking_app.services.FlightService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightMapper flightMapper;
    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightMapper flightMapper, FlightRepository flightRepository) {
        this.flightMapper = flightMapper;
        this.flightRepository = flightRepository;
    }

    @Override
    public FlightResponse createFlight(FlightRequest request) {
        // Ánh xạ từ DTO sang POJO
        Flight flight = flightMapper.fromRequestToPojo(request);
        // Lưu thông tin chuyến bay
        Flight savedFlight = flightRepository.save(flight);
        // Ánh xạ từ POJO sang Response DTO
        return flightMapper.fromPojoToResponse(savedFlight);
    }

    @Override
    public FlightResponse getFlightById(String flightId) {
        // Lấy thông tin chuyến bay từ repository
        Flight flight = flightRepository.findByFlightId(flightId);
        if (flight == null) {
            throw new RuntimeException("Không tìm thấy chuyến bay với flightId: " + flightId);
        }
        // Ánh xạ từ POJO sang Response DTO
        return flightMapper.fromPojoToResponse(flight);
    }

    @Override
    public PaginatedResponse<FlightResponse> getAllFlights(int page, int size) {
        // Lấy danh sách tất cả chuyến bay
        List<Flight> flights = flightRepository.findAll(page, size);
        // Ánh xạ từ POJO sang Response DTO
        List<FlightResponse> flightResponses = flights.stream()
                .map(flightMapper::fromPojoToResponse)
                .collect(Collectors.toList());
        long totalItems = flightRepository.count();
        return getPaginatedResponse(page, size, flightResponses, totalItems);
    }

    @Override
    public void deleteFlight(String flightId) {
        // Xóa chuyến bay
        if (!flightRepository.deleteByFlightId(flightId)) {
            throw new RuntimeException("Không tìm thấy chuyến bay với flightId: " + flightId);
        }
    }

    @Override
    public FlightResponse updateFlight(FlightRequest flightRequest, String flightId) {
        // Tìm chuyến bay hiện tại
        Flight existingFlight = flightRepository.findByFlightId(flightId);
        if (existingFlight == null) {
            throw new RuntimeException("Không tìm thấy chuyến bay với flightId: " + flightId);
        }
        // Cập nhật thông tin chuyến bay
        flightMapper.updateFromRequestToPojo(flightRequest, existingFlight);
        // Lưu thông tin đã cập nhật
        Flight updatedFlight = flightRepository.update(flightId, existingFlight);
        // Ánh xạ từ POJO sang Response DTO
        return flightMapper.fromPojoToResponse(updatedFlight);
    }

    @Override
    public PaginatedResponse<FlightResponse> searchFlights(List<SearchFlightRequest> filters, int page, int size) {
        // Tìm kiếm chuyến bay dựa trên các bộ lọc
        List<Flight> flights = flightRepository.search(filters, page, size);
        // Ánh xạ từ POJO sang Response DTO
        List<FlightResponse> flightResponses = flights.stream()
                .map(flightMapper::fromPojoToResponse)
                .collect(Collectors.toList());
        long totalItems = flightRepository.countSearch(filters);
        // Chia thành các trang
        return getPaginatedResponse(page, size, flightResponses, totalItems);
    }

    private PaginatedResponse<FlightResponse> getPaginatedResponse(int page, int size, List<FlightResponse> flightResponses, long totalItems) {
        PaginatedResponse<FlightResponse> response = new PaginatedResponse<>();
        response.setContent(flightResponses);
        response.setTotalElements(totalItems);
        response.setTotalPages((int) Math.ceil((double) totalItems / size));
        response.setCurrentPage(page);

        return response;
    }
}
