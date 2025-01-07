package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.dto.mapper.UserMapper;
import com.example.airbookingapp.air_booking_app.dto.request.UserRequest;
import com.example.airbookingapp.air_booking_app.dto.response.UserResponse;
import com.example.airbookingapp.air_booking_app.entity.User;
import com.example.airbookingapp.air_booking_app.repositories.UserRepository;
import com.example.airbookingapp.air_booking_app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse saveUser(UserRequest newUserRequest) {
        boolean isUsernameExist = userRepository.existsByUsername(newUserRequest.getUsername());
        if (isUsernameExist) {
            throw new RuntimeException("Username " + newUserRequest.getUsername() + " is already taken.");
        }
        User user = userMapper.MAPPER.fromRequestToEntity(newUserRequest);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAdmin(false);
        userRepository.save(user);
        return userMapper.MAPPER.fromEntityToResponse(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username " + username + " is not found."));
        return userMapper.MAPPER.fromEntityToResponse(user);
    }
}
