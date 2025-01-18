package com.example.airbookingapp.air_booking_app.repositories;

import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Ticket;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.User;
import com.example.airbookingapp.air_booking_app.jooq.tables.records.UserRecord;
import com.example.airbookingapp.air_booking_app.jooq.Tables;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final DSLContext dsl;

    public User save(User user) {
        UserRecord record = dsl.newRecord(Tables.USER, user);
        record.store();
        return record.into(User.class);
    }

    public Optional<User> findByUsername(String username) {
        return dsl.selectFrom(Tables.USER)
                .where(Tables.USER.USERNAME.eq(username))
                .fetchOptionalInto(User.class);
    }

    public boolean existsByUsername(String username) {
        return dsl.fetchExists(
                dsl.selectFrom(Tables.USER)
                        .where(Tables.USER.USERNAME.eq(username))
        );
    }

    public List<Integer> findAffectedUsers(String flightId) {
        return dsl.selectDistinct(Tables.USER.USER_ID)
                .from(Tables.USER)
                .join(Tables.BOOKING)
                .on(Tables.USER.USER_ID.eq(Tables.BOOKING.USER_ID))
                .where(Tables.BOOKING.FLIGHT_ID.eq(flightId))
                .fetchInto(Integer.class);
    }

    public List<Ticket> findAllTicketsByUserId(Integer userId) {
        return dsl.selectFrom(Tables.TICKET)
                .where(Tables.TICKET.USER_ID.eq(userId))
                .fetchInto(Ticket.class);
    }
}
