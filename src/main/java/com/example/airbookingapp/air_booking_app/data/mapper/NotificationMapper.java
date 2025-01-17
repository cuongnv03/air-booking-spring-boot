package com.example.airbookingapp.air_booking_app.data.mapper;

import com.example.airbookingapp.air_booking_app.data.request.NotificationRequest;
import com.example.airbookingapp.air_booking_app.data.response.NotificationResponse;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "notiId", ignore = true)
    Notification fromRequestToPojo(NotificationRequest notificationRequest);

    NotificationResponse fromPojoToResponse(Notification notification);
}
