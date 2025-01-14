package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.data.mapper.FlightMapper;
import com.example.airbookingapp.air_booking_app.data.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.data.request.SearchFlightRequest;
import com.example.airbookingapp.air_booking_app.data.response.FlightResponse;
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
    public List<Page<FlightResponse>> getAllFlights(int sizePerPage) {
        // Lấy danh sách tất cả chuyến bay
        List<Flight> flights = flightRepository.findAll();
        // Ánh xạ từ POJO sang Response DTO
        List<FlightResponse> flightResponses = flights.stream()
                .map(flightMapper::fromPojoToResponse)
                .collect(Collectors.toList());
        // Chia thành các trang
        return paginate(flightResponses, sizePerPage);
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
    public List<Page<FlightResponse>> searchFlights(List<SearchFlightRequest> filters, int sizePerPage) {
        // Tìm kiếm chuyến bay dựa trên các bộ lọc
        List<Flight> flights = flightRepository.search(filters);
        // Ánh xạ từ POJO sang Response DTO
        List<FlightResponse> flightResponses = flights.stream()
                .map(flightMapper::fromPojoToResponse)
                .collect(Collectors.toList());
        // Chia thành các trang
        return paginate(flightResponses, sizePerPage);
    }

    // Hàm phụ để chia dữ liệu thành các trang
    private List<Page<FlightResponse>> paginate(List<FlightResponse> responses, int sizePerPage) {
        List<Page<FlightResponse>> pages = new ArrayList<>();
        int totalItems = responses.size();
        int totalPages = (int) Math.ceil((double) totalItems / sizePerPage);

        for (int pageIndex = 0; pageIndex < totalPages; pageIndex++) {
            int start = pageIndex * sizePerPage;
            int end = Math.min(start + sizePerPage, totalItems);
            List<FlightResponse> pageContent = responses.subList(start, end);
            Page<FlightResponse> page = new PageImpl<>(pageContent, PageRequest.of(pageIndex, sizePerPage), totalItems);
            pages.add(page);
        }
        return pages;
    }

}
