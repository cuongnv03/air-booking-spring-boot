package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.data.request.PaymentRequest;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Booking;
import com.example.airbookingapp.air_booking_app.repositories.BookingRepository;
import com.example.airbookingapp.air_booking_app.repositories.CardRepository;
import com.example.airbookingapp.air_booking_app.security.services.UserDetailsImpl;
import com.example.airbookingapp.air_booking_app.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final BookingRepository bookingRepository;
    private final CardRepository cardRepository;

    public PaymentServiceImpl(BookingRepository bookingRepository, CardRepository cardRepository) {
        this.bookingRepository = bookingRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    @Transactional
    public void processPayment(String bookingId, PaymentRequest paymentRequest, PaymentAction action) {
        // Retrieve authenticated user's details
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        // Fetch booking details
        var booking = bookingRepository.findByBookingId(bookingId);
        if (booking == null) {
            throw new ResourceNotFoundException("Booking not found with ID: " + bookingId);
        }

        // Determine the action
        switch (action) {
            case PAY:
                handlePayment(booking, paymentRequest, userDetails);
                break;
            case REFUND:
                handleRefund(booking, paymentRequest, userDetails);
                break;
            default:
                throw new IllegalArgumentException("Unsupported payment action: " + action);
        }
    }

    private void handlePayment(Booking booking, PaymentRequest paymentRequest, UserDetailsImpl userDetails) {
        if (booking.getPaymentStatus()) {
            throw new IllegalStateException("Booking has already been paid.");
        }
        // Fetch seat price
        Long seatPrice = booking.getAmountPayable();
        if (seatPrice == null) {
            throw new ResourceNotFoundException("Seat price not found for booking ID: " + booking.getBookingId());
        }
        validateAndProcessPayment(userDetails.getUserId(), paymentRequest, seatPrice);
        bookingRepository.updateAmountPayable(booking.getBookingId(), 0);
        bookingRepository.updatePaymentStatus(booking.getBookingId(), true);
    }

    private void handleRefund(Booking booking, PaymentRequest paymentRequest, UserDetailsImpl userDetails) {
        Long seatPrice = bookingRepository.getBookedSeatPrice(booking.getBookingId());
        if (seatPrice == null) {
            throw new ResourceNotFoundException("Seat price not found for booking ID: " + booking.getBookingId());
        }
        validateAndProcessPayment(userDetails.getUserId(), paymentRequest, -seatPrice);
    }

    private void validateAndProcessPayment(Integer userId, PaymentRequest paymentRequest, Long amount) {
        var card = cardRepository.findCardByDetails(
                userId,
                paymentRequest.getCardNumber(),
                paymentRequest.getCardholderName(),
                paymentRequest.getCvvCode()
        );
        if (card == null) {
            throw new ResourceNotFoundException("Invalid card details.");
        }
        // Check balance
        if (card.getBalanceAmount().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new IllegalStateException("Insufficient balance.");
        }
        // Deduct balance
        cardRepository.deductBalance(card.getCardNumber(), amount);
    }
}
