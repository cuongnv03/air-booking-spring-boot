package com.example.airbookingapp.air_booking_app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Flight {
    @Id
    private String flightId;
    @Column(nullable = false)
    private String airline;
//    @ManyToOne
//    private Airport departurePoint;
//    @ManyToOne
//    private Airport destinationPoint;

    @Column(nullable = false)
    private String departurePoint;
    @Column(nullable = false)
    private String destinationPoint;
    @Column(nullable = false)
    private LocalDateTime departureTime;
    @Column(nullable = false)
    private LocalDateTime returnTime;

    private LocalTime duration;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "flight")
    private List<Seat> seats;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "flight")
    private List<Booking> bookings;
}
