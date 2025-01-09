package com.example.airbookingapp.air_booking_app.data.response;

import lombok.Data;

@Data
public class SeatResponse { // This DTO is used when returning seat information to the client.
    private String seatId;
    private String seatClass; // "Business" or "Economy"
    private int baggageAllowance;
    private int price;
    private String seatStatus; // "BOOKED" or "NOT BOOKED"
    private String flightId;
}
