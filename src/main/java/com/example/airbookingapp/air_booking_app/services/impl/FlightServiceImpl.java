package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.dto.mapper.FlightMapper;
import com.example.airbookingapp.air_booking_app.dto.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.dto.request.SearchFlightRequest;
import com.example.airbookingapp.air_booking_app.dto.response.FlightResponse;
import com.example.airbookingapp.air_booking_app.dto.response.PageResponse;
import com.example.airbookingapp.air_booking_app.entity.Flight;
import com.example.airbookingapp.air_booking_app.repositories.FlightRepository;
import com.example.airbookingapp.air_booking_app.services.FlightService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
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
    public PageResponse<FlightResponse> getAllFlights(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("flightId").ascending());
        Page<Flight> pagination = flightRepository.findAll(pageable);
        return PageResponse.<FlightResponse>builder()
                .currentPage(page)
                .totalPages(pagination.getTotalPages())
                .pageSize(size)
                .totalElements(pagination.getTotalElements())
                .data(pagination.getContent().stream().map(flightMapper::fromEntityToResponse).toList())
                .build();
    }

    @Override
    public FlightResponse findByFlightId(String flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight ID " + flightId + " is not found."));
        return flightMapper.fromEntityToResponse(flight);
    }

    @Override
    public void deleteFlight(String flightId) {
        flightRepository.deleteById(flightId);
    }

    @Override
    public FlightResponse saveFlight(FlightRequest flightRequest) {
        Flight flight = flightMapper.fromRequestToEntity(flightRequest);
        flightRepository.save(flight);
        return flightMapper.fromEntityToResponse(flight);
    }

    @Override
    public FlightResponse updateFlight(FlightRequest flightRequest, String flightId) {
        FlightResponse checkExistingFlight = findByFlightId(flightId);
        if (checkExistingFlight != null) {
            Flight flight = flightMapper.fromRequestToEntity(flightRequest);
            flight.setFlightId(flightId);
            flightRepository.save(flight);
            return flightMapper.fromEntityToResponse(flight);
        } else {
            throw new RuntimeException("Flight ID " + flightId + " is not found.");
        }
    }

    @Override
    public PageResponse<FlightResponse> searchFlights(List<SearchFlightRequest> filters, int page, int size) {
        Specification<Flight> specification = buildSpecification(filters);
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("flightId").ascending());
        Page<Flight> pagination = flightRepository.findAll(specification, pageable);
        return PageResponse.<FlightResponse>builder()
                .currentPage(page)
                .totalPages(pagination.getTotalPages())
                .pageSize(size)
                .totalElements(pagination.getTotalElements())
                .data(pagination.getContent().stream().map(flightMapper::fromEntityToResponse).toList())
                .build();
    }

    private Specification<Flight> buildSpecification(List<SearchFlightRequest> filters) {
        if (filters == null || filters.isEmpty()) {
            return Specification.where(null);
        }
        Specification<Flight> specification = Specification.where(createSpecification(filters.removeFirst()));
        for (SearchFlightRequest filter : filters) {
            specification = specification.and(createSpecification(filter));
        }
        return specification;
    }

    private Specification<Flight> createSpecification(SearchFlightRequest searchFlightRequest) {
        return switch (searchFlightRequest.getOperator()) {
            case ":" ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                            root.get(searchFlightRequest.getKey()), castToRequiredType(searchFlightRequest.getKey(), searchFlightRequest.getValue()));
            case "<" ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(
                            root.get(searchFlightRequest.getKey()), (Comparable) castToRequiredType(searchFlightRequest.getKey(), searchFlightRequest.getValue()));
            case ">" ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(
                            root.get(searchFlightRequest.getKey()), (Comparable) castToRequiredType(searchFlightRequest.getKey(), searchFlightRequest.getValue()));
            case "<=" ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                            root.get(searchFlightRequest.getKey()), (Comparable) castToRequiredType(searchFlightRequest.getKey(), searchFlightRequest.getValue()));
            case ">=" ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                            root.get(searchFlightRequest.getKey()), (Comparable) castToRequiredType(searchFlightRequest.getKey(), searchFlightRequest.getValue()));
            default ->
                    throw new IllegalArgumentException("Unsupported operator: " + searchFlightRequest.getOperator());
        };
    }

    private Object castToRequiredType(String key, String value) {
        if (value == null) return null;
        return switch (key) {
            case "departureTime", "returnTime" -> LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
            default -> value; // Assume string for other fields
        };
    }

//    private List<Object> castToRequiredType(Class<?> keyType, List<String> values) {
//        return values.stream()
//                .map(value -> castToRequiredType(keyType, Collections.singletonList(value)))
//                .collect(Collectors.toList());
//    }
}
