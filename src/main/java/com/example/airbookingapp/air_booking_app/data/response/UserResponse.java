package com.example.airbookingapp.air_booking_app.data.response;

import lombok.Data;

@Data
public class UserResponse {
    Integer userId;
    private String username;
    private boolean isAdmin;
}
