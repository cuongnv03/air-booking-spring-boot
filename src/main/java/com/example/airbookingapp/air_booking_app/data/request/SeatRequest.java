package com.example.airbookingapp.air_booking_app.data.request;

import lombok.Data;

@Data
public class SeatRequest {
    private String seatClass; // Loại ghế: "economy" hoặc "business"
    private Boolean seatStatus; // Trạng thái ghế: TRUE nếu đã đặt, FALSE nếu còn trống
}