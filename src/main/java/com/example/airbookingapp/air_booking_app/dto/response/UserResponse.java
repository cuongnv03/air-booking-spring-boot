package com.example.airbookingapp.air_booking_app.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserResponse {
    Integer userId;
    private String username;
    private boolean isAdmin;
}
