package com.example.airbookingapp.air_booking_app.dto.mapper;

import com.example.airbookingapp.air_booking_app.dto.request.UserRequest;
import com.example.airbookingapp.air_booking_app.dto.response.UserResponse;
import com.example.airbookingapp.air_booking_app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
    User fromRequestToEntity(UserRequest userRequest);
    UserResponse fromEntityToResponse(User user);
}
