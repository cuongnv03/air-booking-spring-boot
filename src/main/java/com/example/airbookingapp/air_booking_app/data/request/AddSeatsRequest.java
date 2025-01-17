package com.example.airbookingapp.air_booking_app.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSeatsRequest {
    private int businessSeats;
    private int economySeats;
}
