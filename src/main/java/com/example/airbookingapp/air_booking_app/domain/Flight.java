package com.example.airbookingapp.air_booking_app.domain;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Flight {
    @Id
    private String flightId;

    @Column(nullable = false)
    private String airline;

//    @ManyToOne
//    @JoinColumn(name = "departure_point", referencedColumnName = "airportCode", nullable = false)
//    private Airport departurePoint;
//
//    @ManyToOne
//    @JoinColumn(name = "destination_point", referencedColumnName = "airportCode", nullable = false)
//    private Airport destinationPoint;
    private String departurePoint;

    private String destinationPoint;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime returnTime;

    @Column(columnDefinition = "TIME(6)")
    private LocalTime duration;

    @OneToMany(mappedBy = "flight")
    private List<Seat> seats;

    @OneToMany(mappedBy = "flight")
    private List<Booking> bookings;

    public Flight() {
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

//    public Airport getDeparturePoint() {
//        return departurePoint;
//    }
//
//    public void setDeparturePoint(Airport departurePoint) {
//        this.departurePoint = departurePoint;
//    }
//
//    public Airport getDestinationPoint() {
//        return destinationPoint;
//    }
//
//    public void setDestinationPoint(Airport destinationPoint) {
//        this.destinationPoint = destinationPoint;
//    }


    public String getDeparturePoint() {
        return departurePoint;
    }

    public void setDeparturePoint(String departurePoint) {
        this.departurePoint = departurePoint;
    }

    public String getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(String destinationPoint) {
        this.destinationPoint = destinationPoint;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDateTime returnTime) {
        this.returnTime = returnTime;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }
}
