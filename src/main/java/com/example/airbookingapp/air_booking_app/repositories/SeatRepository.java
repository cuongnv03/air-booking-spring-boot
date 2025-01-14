package com.example.airbookingapp.air_booking_app.repositories;

import com.example.airbookingapp.air_booking_app.jooq.Tables;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Seat;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SeatRepository {

    private final DSLContext dsl;

    public SeatRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    // Tìm ghế theo flightId và seatId
    public Seat findByFlightIdAndSeatId(String flightId, Integer seatId) {
        return dsl.selectFrom(Tables.SEAT)
                .where(Tables.SEAT.FLIGHT_ID.eq(flightId))
                .and(Tables.SEAT.ID.eq(seatId))
                .fetchOneInto(Seat.class);
    }

    // Tìm tất cả ghế của một chuyến bay
    public List<Seat> findByFlightId(String flightId) {
        return dsl.selectFrom(Tables.SEAT)
                .where(Tables.SEAT.FLIGHT_ID.eq(flightId))
                .fetchInto(Seat.class);
    }

    // Tìm tất cả ghế còn trống của một chuyến bay
    public List<Seat> findAvailableSeatsByFlight(String flightId) {
        return dsl.selectFrom(Tables.SEAT)
                .where(Tables.SEAT.FLIGHT_ID.eq(flightId))
                .and(Tables.SEAT.SEAT_STATUS.isFalse())
                .fetchInto(Seat.class);
    }

    // Lưu một ghế mới
    public Seat save(Seat seat) {
        dsl.insertInto(Tables.SEAT)
                .set(dsl.newRecord(Tables.SEAT, seat))
                .execute();
        return findByFlightIdAndSeatId(seat.getFlightId(), seat.getId());
    }

    // Cập nhật thông tin ghế
    public Seat update(String flightId, Integer seatId, Seat seat) {
        dsl.update(Tables.SEAT)
                .set(dsl.newRecord(Tables.SEAT, seat))
                .where(Tables.SEAT.FLIGHT_ID.eq(flightId))
                .and(Tables.SEAT.ID.eq(seatId))
                .execute();
        return findByFlightIdAndSeatId(flightId, seatId);
    }

    public void updateSeatStatus(String flightId, Integer seatId, Boolean seatStatus) {
        dsl.update(Tables.SEAT)
                .set(Tables.SEAT.SEAT_STATUS, seatStatus)
                .where(Tables.SEAT.FLIGHT_ID.eq(flightId))
                .and(Tables.SEAT.ID.eq(seatId))
                .execute();
    }

    // Xóa một ghế
    public boolean delete(String flightId, Integer seatId) {
        int rowsAffected = dsl.deleteFrom(Tables.SEAT)
                .where(Tables.SEAT.FLIGHT_ID.eq(flightId))
                .and(Tables.SEAT.ID.eq(seatId))
                .execute();
        return rowsAffected > 0;
    }
}
