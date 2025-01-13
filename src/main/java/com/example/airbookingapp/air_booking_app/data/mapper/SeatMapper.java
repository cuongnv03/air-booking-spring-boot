package com.example.airbookingapp.air_booking_app.data.mapper;

import com.example.airbookingapp.air_booking_app.data.request.SeatRequest;
import com.example.airbookingapp.air_booking_app.data.response.SeatResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    SeatResponse fromPojoToResponse(Seat seat);
    @Mapping(target = "seatId", ignore = true)
    Seat fromRequestToPojo(SeatRequest seatRequest);

    void updateFromRequestToPojo(SeatRequest seatRequest, @MappingTarget Seat seat);
}
