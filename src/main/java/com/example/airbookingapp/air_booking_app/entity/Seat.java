package com.example.airbookingapp.air_booking_app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Seat {
    @Id
    private String seatId;
    private String seatClass;
    private Integer baggageAllowance;
    private Integer price;
    private String seatStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @OneToOne(cascade = CascadeType.ALL)
    private Booking booking;

    public Seat() {
    }
}
