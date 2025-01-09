package com.example.airbookingapp.air_booking_app.dto.mapper;

import com.example.airbookingapp.air_booking_app.dto.request.SeatRequest;
import com.example.airbookingapp.air_booking_app.dto.response.SeatResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Seat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    SeatResponse fromPojoToResponse(Seat seat);
    Seat fromRequestToPojo(SeatRequest seatRequest);
}
