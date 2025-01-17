package com.example.airbookingapp.air_booking_app.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private String flightId;
    private String notiType;
    private String notiMessage;
    private LocalDateTime createdAt;
}
