package com.example.airbookingapp.air_booking_app.data.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingResponse {
    private String bookingId; // Mã định danh duy nhất cho booking
    private String flightId; // Mã chuyến bay liên quan đến booking
    private Integer seatId; // ID ghế liên quan đến booking
    private Integer userId; // ID người dùng thực hiện booking
    private Long amountPayable; // Số tiền cần thanh toán
    private Boolean paymentStatus; // Trạng thái thanh toán (TRUE: đã thanh toán, FALSE: chưa thanh toán)
    private LocalDateTime createdAt; // Thời gian tạo booking
    private LocalDateTime updatedAt; // Thời gian cập nhật booking
}
