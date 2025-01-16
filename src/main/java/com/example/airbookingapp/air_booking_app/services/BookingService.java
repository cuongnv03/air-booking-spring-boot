package com.example.airbookingapp.air_booking_app.services;

import com.example.airbookingapp.air_booking_app.data.request.BookingRequest;
import com.example.airbookingapp.air_booking_app.data.request.PaymentRequest;
import com.example.airbookingapp.air_booking_app.data.response.BookingResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(BookingRequest bookingRequest);

    BookingResponse getBookingById(String bookingId);

    List<BookingResponse> getAllBookingsByUser();

    // Xóa một booking theo ID
    void deleteBooking(String bookingId);

    // Cập nhật trạng thái thanh toán của booking
    void updatePaymentStatus(String bookingId, boolean paymentStatus);

    Long getBookedSeatPrice(String bookingId);

    @Transactional
    void payBooking(String bookingId, PaymentRequest paymentRequest);
}
