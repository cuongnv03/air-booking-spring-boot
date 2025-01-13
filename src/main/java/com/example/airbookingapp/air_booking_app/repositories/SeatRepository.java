package com.example.airbookingapp.air_booking_app.repositories;

import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Seat;
import com.example.airbookingapp.air_booking_app.jooq.Tables;
import com.example.airbookingapp.air_booking_app.jooq.tables.records.FlightRecord;
import com.example.airbookingapp.air_booking_app.jooq.tables.records.SeatRecord;
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
    public Seat updateSeatAttributes(String seatId, Seat seat) {
        SeatRecord record = dsl.newRecord(Tables.SEAT);
        record.from(seat); // Map POJO to Record
        int rowsAffected = dsl.update(Tables.SEAT)
                .set(record)
                .where(Tables.SEAT.SEAT_ID.eq(seatId))
                .execute();

        if (rowsAffected > 0) {
            return findById(seatId); // Return the updated flight as a POJO
        } else {
            throw new IllegalStateException("Seat with ID " + seatId + " not found.");
        }
    }
}
