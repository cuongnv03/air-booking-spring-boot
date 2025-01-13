package com.example.airbookingapp.air_booking_app.data.request;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    private boolean isAdmin;
}
