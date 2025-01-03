package com.example.airbookingapp.air_booking_app.domain;

import jakarta.persistence.*;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Card {
    @Id
    private Long cardNumber;

    @Column(nullable = false)
    private String cardholderName;

    @Column(nullable = false)
    private LocalDate expireDate;

    @Column(nullable = false)
    private Integer cvvCode;

    @Column(nullable = false)
    private Integer balance;

    public Card() {
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getCvvCode() {
        return cvvCode;
    }

    public void setCvvCode(Integer cvvCode) {
        this.cvvCode = cvvCode;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
