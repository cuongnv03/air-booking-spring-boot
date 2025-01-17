package com.example.airbookingapp.air_booking_app.repositories;

import com.example.airbookingapp.air_booking_app.jooq.Tables;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Booking;
import com.example.airbookingapp.air_booking_app.jooq.tables.records.BookingRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingRepository {

    private final DSLContext dsl;

    public BookingRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    // Lưu một booking mới
    public Booking save(Booking booking) {
        BookingRecord record = dsl.newRecord(Tables.BOOKING, booking); // Ánh xạ POJO -> Record
        record.store(); // Lưu vào database
        // Retrieve the saved booking with the generated bookingId
        return dsl.selectFrom(Tables.BOOKING)
                .where(Tables.BOOKING.ID.eq(record.getId()))
                .fetchOneInto(Booking.class);
    }

    // Tìm booking theo bookingId
    public Booking findByBookingId(String bookingId) {
        return dsl.selectFrom(Tables.BOOKING)
                .where(Tables.BOOKING.BOOKING_ID.eq(bookingId))
                .fetchOneInto(Booking.class);
    }

    // Tìm tất cả các booking theo userId
    public List<Booking> findAllByUserId(Integer userId) {
        return dsl.selectFrom(Tables.BOOKING)
                .where(Tables.BOOKING.USER_ID.eq(userId))
                .fetchInto(Booking.class);
    }

    // Xóa booking theo bookingId
    public boolean deleteById(String bookingId) {
        return dsl.deleteFrom(Tables.BOOKING)
                .where(Tables.BOOKING.BOOKING_ID.eq(bookingId))
                .execute() > 0;
    }

    // Kiểm tra ghế đã được đặt chưa
    public boolean isSeatBooked(String flightId, Integer seatId) {
        return dsl.fetchExists(
                dsl.selectFrom(Tables.BOOKING)
                        .where(Tables.BOOKING.FLIGHT_ID.eq(flightId))
                        .and(Tables.BOOKING.SEAT_ID.eq(seatId))
        );
    }

    public void updateAmountPayable(String bookingId, long amountPayable) {
        dsl.update(Tables.BOOKING)
                .set(Tables.BOOKING.AMOUNT_PAYABLE, amountPayable)
                .where(Tables.BOOKING.BOOKING_ID.eq(bookingId))
                .execute();
    }

    // Cập nhật trạng thái thanh toán
    public void updatePaymentStatus(String bookingId, boolean paymentStatus) {
        dsl.update(Tables.BOOKING)
                .set(Tables.BOOKING.PAYMENT_STATUS, paymentStatus)
                .where(Tables.BOOKING.BOOKING_ID.eq(bookingId))
                .execute();
    }

    public Long getBookedSeatPrice(String bookingId) {
        return dsl.select(Tables.SEAT.PRICE)
                .from(Tables.BOOKING)
                .join(Tables.SEAT)
                .on(Tables.BOOKING.FLIGHT_ID.eq(Tables.SEAT.FLIGHT_ID)
                        .and(Tables.BOOKING.SEAT_ID.eq(Tables.SEAT.ID)))
                .where(Tables.BOOKING.BOOKING_ID.eq(bookingId))
                .fetchOneInto(Long.class);
    }
}
