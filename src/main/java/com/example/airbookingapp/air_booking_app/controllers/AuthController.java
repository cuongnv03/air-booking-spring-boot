package com.example.airbookingapp.air_booking_app.controllers;

import com.example.airbookingapp.air_booking_app.data.request.UserRequest;
import com.example.airbookingapp.air_booking_app.data.response.NotificationResponse;
import com.example.airbookingapp.air_booking_app.data.response.TicketResponse;
import com.example.airbookingapp.air_booking_app.data.response.UserResponse;
import com.example.airbookingapp.air_booking_app.data.response.JwtLoginSuccessResponse;
import com.example.airbookingapp.air_booking_app.data.request.LoginRequest;
import com.example.airbookingapp.air_booking_app.security.jwt.JwtUtils;
import com.example.airbookingapp.air_booking_app.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    // Log in
    @PostMapping("/auth/login")
    public JwtLoginSuccessResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        String username = jwtUtils.getUserNameFromJwtToken(jwt); // Retrieve the username from the authentication object
        String expiryDate = jwtUtils.getExpirationDateFromToken(jwt).toString(); // Assuming jwtUtils can parse expiration date

        return new JwtLoginSuccessResponse(true, jwt, username, expiryDate);
    }

    // Register
    @PostMapping("/auth/register")
    public UserResponse registerUser(@Valid @RequestBody UserRequest userRequest) {
        return userService.saveUser(userRequest);
    }

    @GetMapping("/notifications")
    public List<NotificationResponse> getNotificationsByUserId() {
        return userService.getNotificationsByUserId();
    }

    @GetMapping("/tickets")
    public List<TicketResponse> getTicketsByUserId() {
        return userService.getTicketsByUserId();
    }
}
