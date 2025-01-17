package com.example.airbookingapp.air_booking_app.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    Integer userId;
    private String username;
    private boolean isAdmin;
}
