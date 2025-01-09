package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.data.mapper.SeatMapper;
import com.example.airbookingapp.air_booking_app.data.response.SeatResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Seat;
import com.example.airbookingapp.air_booking_app.repositories.SeatRepository;
import com.example.airbookingapp.air_booking_app.services.SeatService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;

    public SeatServiceImpl(SeatRepository seatRepository, SeatMapper seatMapper) {
        this.seatRepository = seatRepository;
        this.seatMapper = seatMapper;
    }

    @Override
    public SeatResponse getSeatDetails(String seatId) {
        Seat seat = seatRepository.findById(seatId);
        if (seat == null) {
            throw new RuntimeException("Seat with ID " + seatId + " not found.");
        }
        return seatMapper.fromPojoToResponse(seat);
    }

    @Override
    public List<SeatResponse> getAvailableSeatsByFlight(String flightId) {
        List<Seat> availableSeats = seatRepository.findAvailableSeatsByFlight(flightId);
        return availableSeats.stream()
                .map(seatMapper::fromPojoToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SeatResponse updateSeatStatus(String seatId, String status) {
        int rowsAffected = seatRepository.updateSeatStatus(seatId, status);
        if (rowsAffected == 0) {
            throw new RuntimeException("Failed to update status for seat ID " + seatId);
        }
        return getSeatDetails(seatId);
    }

    @Override
    public SeatResponse updateSeatAttributes(String seatId, int baggageAllowance, int price) {
        int rowsAffected = seatRepository.updateSeatAttributes(seatId, baggageAllowance, price);
        if (rowsAffected == 0) {
            throw new RuntimeException("Failed to update attributes for seat ID " + seatId);
        }
        return getSeatDetails(seatId);
    }
}
