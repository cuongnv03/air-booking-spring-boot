package com.example.airbookingapp.air_booking_app.domain;

import jakarta.persistence.*;

@Entity
public class Seat {
    @Id
    private String seatId;

    @Column(nullable = false)
    private String seatClass;

    @Column(nullable = false)
    private Integer baggageAllowance;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String seatStatus;

    @ManyToOne
    @JoinColumn(name = "flight_id", referencedColumnName = "flightId", nullable = false)
    private Flight flight;

    @OneToOne(mappedBy = "seat")
    private Booking booking;

    public Seat() {
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public Integer getBaggageAllowance() {
        return baggageAllowance;
    }

    public void setBaggageAllowance(Integer baggageAllowance) {
        this.baggageAllowance = baggageAllowance;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(String seatStatus) {
        this.seatStatus = seatStatus;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
}
