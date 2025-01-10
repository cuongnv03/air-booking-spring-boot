package com.example.airbookingapp.air_booking_app.data.mapper;

import com.example.airbookingapp.air_booking_app.data.request.FlightRequest;
import com.example.airbookingapp.air_booking_app.data.response.FlightResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FlightMapper {
    Flight fromRequestToPojo(FlightRequest flightRequest);
    FlightResponse fromPojoToResponse(Flight flight);

    @Mapping(target = "flightId", source = "flightId") // Keep flightId unchanged
    void updateFromRequestToPojo(FlightRequest flightRequest, @MappingTarget Flight flight);
}
