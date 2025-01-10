package com.example.airbookingapp.air_booking_app.data.mapper;

import com.example.airbookingapp.air_booking_app.data.request.SeatRequest;
import com.example.airbookingapp.air_booking_app.data.response.SeatResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SeatMapper {
    SeatResponse fromPojoToResponse(Seat seat);
    Seat fromRequestToPojo(SeatRequest seatRequest);
}
