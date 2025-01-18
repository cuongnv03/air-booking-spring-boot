package com.example.airbookingapp.air_booking_app.services;

import com.example.airbookingapp.air_booking_app.data.request.UserRequest;
import com.example.airbookingapp.air_booking_app.data.response.NotificationResponse;
import com.example.airbookingapp.air_booking_app.data.response.TicketResponse;
import com.example.airbookingapp.air_booking_app.data.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse saveUser(UserRequest newUserRequest);
    UserResponse getUserByUsername(String username);
    List<NotificationResponse> getNotificationsByUserId();
    List<TicketResponse> getTicketsByUserId();
}
