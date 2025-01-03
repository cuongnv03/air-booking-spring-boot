package com.example.airbookingapp.air_booking_app.domain;

import jakarta.persistence.*;

@Entity
public class Booking {
    @Id
    private String bookingId;

    @ManyToOne
    @JoinColumn(name = "flight_id", referencedColumnName = "flightId", nullable = false)
    private Flight flight;

    @OneToOne
    @JoinColumn(name = "seat_id", referencedColumnName = "seatId", unique = true, nullable = false)
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String paymentStatus;

    public Booking() {
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
