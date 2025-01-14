package com.example.airbookingapp.air_booking_app.data.mapper;

import com.example.airbookingapp.air_booking_app.data.request.UserRequest;
import com.example.airbookingapp.air_booking_app.data.response.UserResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromRequestToPojo(UserRequest userRequest);
    UserResponse fromPojoToResponse(User user);
}
