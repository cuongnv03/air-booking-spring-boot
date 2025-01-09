package com.example.airbookingapp.air_booking_app.data.mapper;

import com.example.airbookingapp.air_booking_app.data.request.BookingRequest;
import com.example.airbookingapp.air_booking_app.data.response.BookingResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "bookingId", source = "bookingId")
    @Mapping(target = "paymentStatus", source = "paymentStatus")
    Booking fromRequestToPojo(BookingRequest bookingRequest, Integer userId, String bookingId, String paymentStatus);
    BookingResponse fromPojoToResponse(Booking booking);
}
