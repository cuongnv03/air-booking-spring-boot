package com.example.airbookingapp.air_booking_app.data.response;

import lombok.Data;

@Data
public class SeatResponse {
    private Integer seatId; // ID của ghế
    private String seatClass; // Loại ghế: "economy" hoặc "business"
    private Long price; // Giá vé cho ghế
    private Integer baggageAllowance; // Số hành lý ký gửi được phép
    private Boolean seatStatus; // Trạng thái ghế: TRUE nếu đã đặt, FALSE nếu còn trống
    private String flightId; // Mã chuyến bay liên kết với ghế
}
