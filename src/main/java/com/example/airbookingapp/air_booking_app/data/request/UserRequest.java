package com.example.airbookingapp.air_booking_app.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserRequest {
    private String username;
    private String password;
    private boolean isAdmin;
}
