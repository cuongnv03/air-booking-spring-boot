package com.example.airbookingapp.air_booking_app.data.mapper;

import com.example.airbookingapp.air_booking_app.data.request.BookingRequest;
import com.example.airbookingapp.air_booking_app.data.response.BookingResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    // Map từ BookingRequest sang Booking (POJO)
    @Mapping(target = "id", ignore = true) // id được tự động sinh
    @Mapping(target = "bookingId", ignore = true) // bookingId được tự động sinh
    @Mapping(target = "createdAt", ignore = true) // createdAt được tự động sinh bởi trigger
    @Mapping(target = "updatedAt", ignore = true) // updatedAt được tự động sinh bởi trigger
    @Mapping(target = "paymentStatus", constant = "false") // paymentStatus mặc định là FALSE
    Booking fromRequestToPojo(BookingRequest bookingRequest, int userId);

    // Map từ Booking (POJO) sang BookingResponse
    BookingResponse fromPojoToResponse(Booking booking);

    // Cập nhật Booking từ BookingRequest
    @Mapping(target = "id", ignore = true) // Không thay đổi id
    @Mapping(target = "bookingId", ignore = true) // Không thay đổi bookingId
    @Mapping(target = "createdAt", ignore = true) // Không thay đổi createdAt
    void updateFromRequestToPojo(BookingRequest bookingRequest, @MappingTarget Booking booking);
}
