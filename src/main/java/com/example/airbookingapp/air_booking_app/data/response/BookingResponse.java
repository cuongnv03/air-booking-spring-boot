package com.example.airbookingapp.air_booking_app.data.response;

import lombok.Data;

@Data
public class BookingResponse {
    private String bookingId;
    private String flightId;
    private String seatId;
    private Integer userId;
    private String paymentStatus; // e.g., "PAID" or "NOT PAID"
}