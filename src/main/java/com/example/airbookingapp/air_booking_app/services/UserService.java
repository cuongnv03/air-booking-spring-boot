package com.example.airbookingapp.air_booking_app.services;

import com.example.airbookingapp.air_booking_app.data.request.UserRequest;
import com.example.airbookingapp.air_booking_app.data.response.UserResponse;

public interface UserService {
    UserResponse saveUser(UserRequest newUserRequest);
    UserResponse getUserByUsername(String username);
}
