package com.example.airbookingapp.air_booking_app.services;

import com.example.airbookingapp.air_booking_app.data.request.PaymentRequest;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentService {
    @Transactional
    void processPayment(String bookingId, PaymentRequest paymentRequest, PaymentAction action);
    enum PaymentAction {
        PAY,
        REFUND
    }
}
