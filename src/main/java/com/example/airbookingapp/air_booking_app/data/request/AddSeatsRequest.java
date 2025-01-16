package com.example.airbookingapp.air_booking_app.data.request;

import lombok.Data;

@Data
public class AddSeatsRequest {
    private int businessSeats;
    private int economySeats;
}
