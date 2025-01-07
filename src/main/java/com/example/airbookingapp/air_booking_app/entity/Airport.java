package com.example.airbookingapp.air_booking_app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Airport {
    @Id
    private String airportCode;
    private String airportName;
    private String city;
    private String country;

    public Airport() {
    }
}
