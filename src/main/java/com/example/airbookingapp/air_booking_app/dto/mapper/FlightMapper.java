package com.example.airbookingapp.air_booking_app.dto.mapper;

import com.example.airbookingapp.air_booking_app.dto.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.dto.response.FlightResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Flight;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    Flight fromRequestToPojo(FlightRequest flightRequest);
    FlightResponse fromPojoToResponse(Flight flight);
}
