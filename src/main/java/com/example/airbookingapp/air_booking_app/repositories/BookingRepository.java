package com.example.airbookingapp.air_booking_app.repositories;

import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Booking;
import com.example.airbookingapp.air_booking_app.jooq.Tables;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingRepository {

    private final DSLContext dsl;

    public BookingRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    // Save a new booking
    public Booking save(Booking booking) {
        dsl.insertInto(Tables.BOOKING)
                .set(Tables.BOOKING.BOOKING_ID, booking.getBookingId())
                .set(Tables.BOOKING.FLIGHT_ID, booking.getFlightId())
                .set(Tables.BOOKING.SEAT_ID, booking.getSeatId())
                .set(Tables.BOOKING.USER_ID, booking.getUserId())
                .set(Tables.BOOKING.PAYMENT_STATUS, booking.getPaymentStatus())
                .execute();
        return booking;
    }

    // Find a booking by ID
    public Booking findById(String bookingId) {
        return dsl.selectFrom(Tables.BOOKING)
                .where(Tables.BOOKING.BOOKING_ID.eq(bookingId))
                .fetchOneInto(Booking.class);
    }

    // Find all bookings by user ID
    public List<Booking> findAllByUserId(Integer userId) {
        return dsl.selectFrom(Tables.BOOKING)
                .where(Tables.BOOKING.USER_ID.eq(userId))
                .fetchInto(Booking.class);
    }

    // Check if a booking exists for a seat
    public boolean existsBySeatId(String seatId) {
        return dsl.fetchExists(
                dsl.selectFrom(Tables.BOOKING)
                        .where(Tables.BOOKING.SEAT_ID.eq(seatId))
        );
    }
}
