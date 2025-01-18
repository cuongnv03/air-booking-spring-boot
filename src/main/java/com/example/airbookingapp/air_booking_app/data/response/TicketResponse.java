package com.example.airbookingapp.air_booking_app.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {
    String bookingId;
    Integer userId;
    String fullName;
    Date dateOfBirth;
    String gender;
    String contactPhone;
    String email;
    String identityCardId;
    String company;
    String flightId;
    LocalDateTime scheduledTime;
    String departureAirport;
    String destinationAirport;
    Integer seatId;
    Integer baggageAllowance;
    Integer price;
}
