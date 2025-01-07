package com.example.airbookingapp.air_booking_app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Booking {
    @Id
    private String bookingId;
    private String paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @OneToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Booking() {
    }
}
