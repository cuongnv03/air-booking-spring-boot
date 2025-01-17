package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.data.mapper.FlightMapper;
import com.example.airbookingapp.air_booking_app.data.mapper.NotificationMapper;
import com.example.airbookingapp.air_booking_app.data.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.data.request.NotificationRequest;
import com.example.airbookingapp.air_booking_app.data.request.SearchFlightRequest;
import com.example.airbookingapp.air_booking_app.data.response.FlightResponse;
import com.example.airbookingapp.air_booking_app.data.response.PaginatedResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Flight;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Notification;
import com.example.airbookingapp.air_booking_app.repositories.FlightRepository;
import com.example.airbookingapp.air_booking_app.repositories.NotificationRepository;
import com.example.airbookingapp.air_booking_app.repositories.UserRepository;
import com.example.airbookingapp.air_booking_app.services.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightMapper flightMapper;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

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
        // Tìm chuyến bay hiện tại
        Flight existingFlight = flightRepository.findByFlightId(flightId);
        if (existingFlight == null) {
            throw new RuntimeException("Không tìm thấy chuyến bay với flightId: " + flightId);
        }

        List<Integer> affectedUserIds = userRepository.findAffectedUsers(existingFlight.getFlightId());

        // Xóa chuyến bay
        boolean isDeleted = flightRepository.deleteByFlightId(flightId);
        if (!isDeleted) {
            throw new RuntimeException("Không thể xóa chuyến bay với flightId: " + flightId);
        }
        String message = String.format("Chuyến bay %s đã bị hủy.", flightId);
        notifyUsers(affectedUserIds, flightId, "CANCELLED", message);
    }

    @Override
    public FlightResponse updateFlight(FlightRequest flightRequest, String flightId) {
        // Tìm chuyến bay hiện tại
        Flight existingFlight = flightRepository.findByFlightId(flightId);
        if (existingFlight == null) {
            throw new RuntimeException("Không tìm thấy chuyến bay với flightId: " + flightId);
        }
        // Lưu lại thông tin cũ để so sánh
        LocalDateTime oldScheduledTime = existingFlight.getScheduledTime();
        // Cập nhật thông tin chuyến bay
        flightMapper.updateFromRequestToPojo(flightRequest, existingFlight);
        // Lưu thông tin đã cập nhật
        Flight updatedFlight = flightRepository.update(flightId, existingFlight);
        // Kiểm tra thay đổi và gửi thông báo
        if (!oldScheduledTime.equals(updatedFlight.getScheduledTime())) {
            handleScheduledTimeChange(oldScheduledTime, updatedFlight);
        }
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

    private void handleScheduledTimeChange(LocalDateTime oldScheduledTime, Flight updatedFlight) {
        List<Integer> affectedUserIds = userRepository.findAffectedUsers(updatedFlight.getFlightId());
        String type;
        String message;
        if (updatedFlight.getScheduledTime().isAfter(oldScheduledTime)) {
            type = "POSTPONED";
            message = String.format("Chuyến bay %s đã bị hoãn từ %s sang %s.",
                    updatedFlight.getFlightId(),
                    oldScheduledTime,
                    updatedFlight.getScheduledTime());
        } else {
            type = "PREDEPARTED";
            message = String.format("Chuyến bay %s đã được khởi hành sớm từ %s sang %s.",
                    updatedFlight.getFlightId(),
                    oldScheduledTime,
                    updatedFlight.getScheduledTime());
        }
        notifyUsers(affectedUserIds, updatedFlight.getFlightId(), type, message);
    }

    private void notifyUsers(List<Integer> userIds, String flightId, String type, String message) {
        for (Integer userId : userIds) {
            NotificationRequest notificationRequest = new NotificationRequest(userId, flightId, type, message);
            Notification notification = notificationMapper.fromRequestToPojo(notificationRequest);
            notificationRepository.save(notification);
        }
    }
}
