package com.example.airbookingapp.air_booking_app.repositories;

import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Seat;
import com.example.airbookingapp.air_booking_app.jooq.Tables;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SeatRepository {

    private final DSLContext dsl;

    public SeatRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    // Find seat by ID
    public Seat findById(String seatId) {
        return dsl.selectFrom(Tables.SEAT)
                .where(Tables.SEAT.SEAT_ID.eq(seatId))
                .fetchOneInto(Seat.class);
    }

    // List available seats for a flight
    public List<Seat> findAvailableSeatsByFlight(String flightId) {
        return dsl.selectFrom(Tables.SEAT)
                .where(Tables.SEAT.FLIGHT_ID.eq(flightId))
                .and(Tables.SEAT.SEAT_STATUS.eq("NOT BOOKED"))
                .fetchInto(Seat.class);
    }

    // Update seat status
    public int updateSeatStatus(String seatId, String status) {
        return dsl.update(Tables.SEAT)
                .set(Tables.SEAT.SEAT_STATUS, status)
                .where(Tables.SEAT.SEAT_ID.eq(seatId))
                .execute();
    }

    // Update seat attributes
    public int updateSeatAttributes(String seatId, int baggageAllowance, int price) {
        return dsl.update(Tables.SEAT)
                .set(Tables.SEAT.BAGGAGE_ALLOWANCE, baggageAllowance)
                .set(Tables.SEAT.PRICE, price)
                .where(Tables.SEAT.SEAT_ID.eq(seatId))
                .execute();
    }
}
