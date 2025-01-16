package com.example.airbookingapp.air_booking_app.data.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private String cardNumber;
    private String cardholderName;
    private String cvvCode;
}
