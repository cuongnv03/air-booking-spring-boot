package com.example.airbookingapp.air_booking_app.data.mapper;

import com.example.airbookingapp.air_booking_app.data.request.SeatRequest;
import com.example.airbookingapp.air_booking_app.data.response.SeatResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    // Map từ Seat (POJO) sang SeatResponse (DTO)
    @Mapping(target = "seatId", source = "id")
    SeatResponse fromPojoToResponse(Seat seat);

    @Mapping(target = "id", ignore = true) // ID được cung cấp bởi hệ thống
    @Mapping(target = "flightId", ignore = true) // flightId được truyền từ controller
    Seat fromRequestToPojo(SeatRequest seatRequest);

    // Cập nhật dữ liệu từ SeatRequest vào Seat (POJO hiện tại)
    @Mapping(target = "id", ignore = true) // ID không thay đổi
    void updateFromRequestToPojo(SeatRequest seatRequest, @MappingTarget Seat seat);
}
