package com.example.airbookingapp.air_booking_app.data.mapper;

import com.example.airbookingapp.air_booking_app.data.response.TicketResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketResponse fromPojoToResponse(Ticket ticket);
}
