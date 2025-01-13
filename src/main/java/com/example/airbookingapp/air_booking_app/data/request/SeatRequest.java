package com.example.airbookingapp.air_booking_app.data.request;

import lombok.Data;

@Data
public class SeatRequest { // This DTO is used when updating seat attributes or status.
    private String seatId;
    private Integer baggageAllowance;
    private Integer price;
    private String seatStatus; // "BOOKED" or "NOT BOOKED"
}
