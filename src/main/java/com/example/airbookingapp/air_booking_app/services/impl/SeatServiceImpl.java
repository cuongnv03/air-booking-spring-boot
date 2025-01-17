package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.data.mapper.SeatMapper;
import com.example.airbookingapp.air_booking_app.data.request.AddSeatsRequest;
import com.example.airbookingapp.air_booking_app.data.request.SeatRequest;
import com.example.airbookingapp.air_booking_app.data.response.SeatResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Seat;
import com.example.airbookingapp.air_booking_app.repositories.FlightRepository;
import com.example.airbookingapp.air_booking_app.repositories.SeatRepository;
import com.example.airbookingapp.air_booking_app.services.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;
    private final FlightRepository flightRepository;

    @Override
    public SeatResponse getSeatDetails(String flightId, Integer seatId) {
        Seat seat = seatRepository.findByFlightIdAndSeatId(flightId, seatId);
        if (seat == null) {
            throw new RuntimeException("Không tìm thấy ghế với flightId: " + flightId + " và seatId: " + seatId);
        }
        return seatMapper.fromPojoToResponse(seat);
    }

    @Override
    public List<SeatResponse> getAvailableSeats(String flightId) {
        List<Seat> seats = seatRepository.findAvailableSeatsByFlight(flightId);
        return seats.stream()
                .map(seatMapper::fromPojoToResponse)
                .collect(Collectors.toList());
    }

    private Seat addSeat(String flightId, SeatRequest seatRequest) {
        Seat seat = seatMapper.fromRequestToPojo(seatRequest);
        seat.setFlightId(flightId);
        return seat;
    }

    @Override
    public void addSeatsToFlight(String flightId, AddSeatsRequest addSeatsRequest) {
        if (flightRepository.findByFlightId(flightId) == null) {
            throw new RuntimeException("Không tìm thấy chuyến bay với flightId: " + flightId);
        }
        List<Seat> seats = new ArrayList<>();
        while (addSeatsRequest.getBusinessSeats() > 0) {
            Seat seat = addSeat(flightId, new SeatRequest("business", false));
            seats.add(seat);
            addSeatsRequest.setBusinessSeats(addSeatsRequest.getBusinessSeats() - 1);
        }
        while (addSeatsRequest.getEconomySeats() > 0) {
            Seat seat = addSeat(flightId, new SeatRequest("economy", false));
            seats.add(seat);
            addSeatsRequest.setEconomySeats(addSeatsRequest.getEconomySeats() - 1);
        }
        seatRepository.saveSeats(seats);
    }

    @Override
    public SeatResponse updateSeat(String flightId, Integer seatId, SeatRequest seatRequest) {
        Seat existingSeat = seatRepository.findByFlightIdAndSeatId(flightId, seatId);
        if (existingSeat == null) {
            throw new RuntimeException("Không tìm thấy ghế với flightId: " + flightId + " và seatId: " + seatId);
        }
        seatMapper.updateFromRequestToPojo(seatRequest, existingSeat);
        Seat updatedSeat = seatRepository.update(flightId, seatId, existingSeat);
        return seatMapper.fromPojoToResponse(updatedSeat);
    }

    @Override
    public void deleteSeat(String flightId, Integer seatId) {
        if (!seatRepository.delete(flightId, seatId)) {
            throw new RuntimeException("Không thể xóa ghế với flightId: " + flightId + " và seatId: " + seatId);
        }
    }
}
