package com.example.airbookingapp.air_booking_app.controllers;

import com.example.airbookingapp.air_booking_app.data.request.BookingRequest;
import com.example.airbookingapp.air_booking_app.data.request.PaymentRequest;
import com.example.airbookingapp.air_booking_app.data.response.BookingResponse;
import com.example.airbookingapp.air_booking_app.services.BookingService;
import com.example.airbookingapp.air_booking_app.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final PaymentService paymentService;

    public BookingController(BookingService bookingService, PaymentService paymentService) {
        this.bookingService = bookingService;
        this.paymentService = paymentService;
    }

    // Lấy thông tin một booking theo ID
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable String bookingId) {
        BookingResponse bookingResponse = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(bookingResponse);
    }

    // Lấy danh sách tất cả các booking của người dùng hiện tại
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getUserBookings(Authentication authentication) {
        List<BookingResponse> bookings = bookingService.getAllBookingsByUser();
        return ResponseEntity.ok(bookings);
    }

    // Tạo một booking mới
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        BookingResponse bookingResponse = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(bookingResponse);
    }

    @PostMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> changeBooking(@PathVariable String bookingId,
                                                         @Valid @RequestBody BookingRequest bookingRequest) {
        BookingResponse bookingResponse = bookingService.changeBooking(bookingId, bookingRequest);
        return ResponseEntity.ok(bookingResponse);
    }

    // Xóa một booking theo ID
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable String bookingId,
                                                @Valid @RequestBody PaymentRequest paymentRequest) {
        bookingService.cancelBooking(bookingId, paymentRequest);
        return ResponseEntity.ok("Booking ID " + bookingId + " đã được xóa thành công.");
    }

    @PostMapping("/{bookingId}/pay")
    public ResponseEntity<String> payBooking(@PathVariable String bookingId,
                                             @Valid @RequestBody PaymentRequest paymentRequest) {
        paymentService.processPayment(bookingId, paymentRequest, PaymentService.PaymentAction.PAY);
        return ResponseEntity.ok("Booking ID " + bookingId + " đã được thanh toán.");
    }
}
