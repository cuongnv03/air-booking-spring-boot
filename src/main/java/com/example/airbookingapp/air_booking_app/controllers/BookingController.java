package com.example.airbookingapp.air_booking_app.controllers;

import com.example.airbookingapp.air_booking_app.data.request.BookingRequest;
import com.example.airbookingapp.air_booking_app.data.response.BookingResponse;
import com.example.airbookingapp.air_booking_app.services.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Tạo một booking mới
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        BookingResponse bookingResponse = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(bookingResponse);
    }

    // Lấy thông tin một booking theo ID
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable String bookingId) {
        BookingResponse bookingResponse = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(bookingResponse);
    }

    // Lấy danh sách tất cả các booking của người dùng hiện tại
    @GetMapping("/all")
    public ResponseEntity<List<BookingResponse>> getUserBookings(Authentication authentication) {
        List<BookingResponse> bookings = bookingService.getAllBookingsByUser();
        return ResponseEntity.ok(bookings);
    }

    // Xóa một booking theo ID
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> deleteBooking(@PathVariable String bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.ok("Booking ID " + bookingId + " đã được xóa thành công.");
    }

    // Cập nhật trạng thái thanh toán
    @PutMapping("/{bookingId}/payment-status")
    public ResponseEntity<String> updatePaymentStatus(
            @PathVariable String bookingId,
            @RequestParam boolean paymentStatus) {
        bookingService.updatePaymentStatus(bookingId, paymentStatus);
        return ResponseEntity.ok("Trạng thái thanh toán của Booking ID " + bookingId + " đã được cập nhật.");
    }
}
