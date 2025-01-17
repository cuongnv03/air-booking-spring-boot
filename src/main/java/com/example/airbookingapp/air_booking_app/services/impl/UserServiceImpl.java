package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.data.mapper.NotificationMapper;
import com.example.airbookingapp.air_booking_app.data.mapper.UserMapper;
import com.example.airbookingapp.air_booking_app.data.request.UserRequest;
import com.example.airbookingapp.air_booking_app.data.response.NotificationResponse;
import com.example.airbookingapp.air_booking_app.data.response.UserResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.User;
import com.example.airbookingapp.air_booking_app.repositories.NotificationRepository;
import com.example.airbookingapp.air_booking_app.repositories.UserRepository;
import com.example.airbookingapp.air_booking_app.security.services.UserDetailsImpl;
import com.example.airbookingapp.air_booking_app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public UserResponse saveUser(UserRequest newUserRequest) {
        boolean isUsernameExist = userRepository.existsByUsername(newUserRequest.getUsername());
        if (isUsernameExist) {
            throw new RuntimeException("Username " + newUserRequest.getUsername() + " is already taken.");
        }

        User user = userMapper.fromRequestToPojo(newUserRequest);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setIsAdmin(false);

        User savedUser = userRepository.save(user);
        return userMapper.fromPojoToResponse(savedUser);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username " + username + " is not found."));
        return userMapper.fromPojoToResponse(user);
    }

    @Override
    public List<NotificationResponse> getNotificationsByUserId() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return notificationRepository.findByUserId(userDetails.getUserId()).stream()
                .map(notificationMapper::fromPojoToResponse)
                .collect(Collectors.toList());
    }
}
