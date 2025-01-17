package com.example.airbookingapp.air_booking_app.repositories;

import com.example.airbookingapp.air_booking_app.jooq.Tables;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Notification;
import com.example.airbookingapp.air_booking_app.jooq.tables.records.NotificationRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {
    private final DSLContext dsl;

    public void save(Notification notification) {
        NotificationRecord record = dsl.newRecord(Tables.NOTIFICATION);
        record.from(notification);
        record.store();
    }

    public List<Notification> findByUserId(Integer userId) {
        return dsl.selectFrom(Tables.NOTIFICATION)
                .where(Tables.NOTIFICATION.USER_ID.eq(userId))
                .fetchInto(Notification.class);
    }
}
