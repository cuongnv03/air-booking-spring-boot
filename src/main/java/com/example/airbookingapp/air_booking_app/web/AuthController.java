package com.example.airbookingapp.air_booking_app.web;

import com.example.airbookingapp.air_booking_app.dto.request.UserRequest;
import com.example.airbookingapp.air_booking_app.dto.response.UserResponse;
import com.example.airbookingapp.air_booking_app.payload.JwtLoginSuccessResponse;
import com.example.airbookingapp.air_booking_app.payload.LoginRequest;
import com.example.airbookingapp.air_booking_app.security.jwt.JwtUtils;
import com.example.airbookingapp.air_booking_app.security.services.UserDetailsImpl;
import com.example.airbookingapp.air_booking_app.repositories.UserRepository;

import com.example.airbookingapp.air_booking_app.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    // Log in
    @PostMapping("/login")
    public JwtLoginSuccessResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new JwtLoginSuccessResponse(true, jwt);
    }

    // Register
    @PostMapping("/register")
    public UserResponse registerUser(@Valid @RequestBody UserRequest userRequest) {
        return userService.saveUser(userRequest);
    }
}
