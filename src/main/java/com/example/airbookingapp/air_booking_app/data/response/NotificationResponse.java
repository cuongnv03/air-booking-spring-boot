package com.example.airbookingapp.air_booking_app.data.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private String flightId;
    private String notiType;
    private String notiMessage;
    private LocalDateTime createdAt;
}
