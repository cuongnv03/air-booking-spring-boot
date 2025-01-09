package com.example.airbookingapp.air_booking_app.dto.mapper;

import com.example.airbookingapp.air_booking_app.dto.request.UserRequest;
import com.example.airbookingapp.air_booking_app.dto.response.UserResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users fromRequestToPojo(UserRequest userRequest);
    UserResponse fromPojoToResponse(Users user);
}
