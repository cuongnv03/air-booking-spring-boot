package com.example.airbookingapp.air_booking_app.services.impl;

import com.example.airbookingapp.air_booking_app.data.mapper.UserMapper;
import com.example.airbookingapp.air_booking_app.data.request.UserRequest;
import com.example.airbookingapp.air_booking_app.data.response.UserResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Users;
import com.example.airbookingapp.air_booking_app.repositories.UserRepository;
import com.example.airbookingapp.air_booking_app.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, PasswordEncoder encoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public UserResponse saveUser(UserRequest newUserRequest) {
        boolean isUsernameExist = userRepository.existsByUsername(newUserRequest.getUsername());
        if (isUsernameExist) {
            throw new RuntimeException("Username " + newUserRequest.getUsername() + " is already taken.");
        }

        Users user = userMapper.fromRequestToPojo(newUserRequest);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setIsAdmin(false);

        Users savedUser = userRepository.save(user);
        return userMapper.fromPojoToResponse(savedUser);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username " + username + " is not found."));
        return userMapper.fromPojoToResponse(user);
    }
}
