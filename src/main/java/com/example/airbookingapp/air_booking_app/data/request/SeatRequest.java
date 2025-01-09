package com.example.airbookingapp.air_booking_app.dto.request;

import lombok.Data;

@Data
public class SeatRequest { // This DTO is used when updating seat attributes or status.
    private int baggageAllowance;
    private int price;
    private String seatStatus; // "BOOKED" or "NOT BOOKED"
}
