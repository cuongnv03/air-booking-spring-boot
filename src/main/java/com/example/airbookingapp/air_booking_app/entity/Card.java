package com.example.airbookingapp.air_booking_app.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Card {
    @Id
    private Long cardNumber;
    private String cardholderName;
    private LocalDate expireDate;
    private Integer cvvCode;
    private Integer balance;

    public Card() {
    }
}
