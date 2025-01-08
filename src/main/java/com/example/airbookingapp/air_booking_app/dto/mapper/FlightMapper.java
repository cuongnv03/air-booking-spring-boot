package com.example.airbookingapp.air_booking_app.dto.mapper;

import com.example.airbookingapp.air_booking_app.dto.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.dto.response.FlightResponse;
import com.example.airbookingapp.air_booking_app.entity.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    Flight fromRequestToEntity(FlightRequest flightRequest);

    default String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

//    @Mapping(target = "departureTime", expression = "java(formatDateTime(flight.getDepartureTime()))")
//    @Mapping(target = "returnTime", expression = "java(formatDateTime(flight.getReturnTime()))")
    FlightResponse fromEntityToResponse(Flight flight);
}
