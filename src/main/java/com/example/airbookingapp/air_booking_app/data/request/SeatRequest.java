package com.example.airbookingapp.air_booking_app.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatRequest {
    private String seatClass; // Loại ghế: "economy" hoặc "business"
    private Boolean seatStatus; // Trạng thái ghế: TRUE nếu đã đặt, FALSE nếu còn trống
}