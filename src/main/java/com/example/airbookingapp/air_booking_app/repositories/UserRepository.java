package com.example.airbookingapp.air_booking_app.repositories;

import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.User;
import com.example.airbookingapp.air_booking_app.jooq.tables.records.UserRecord;
import com.example.airbookingapp.air_booking_app.jooq.Tables;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private final DSLContext dsl;

    public UserRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

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
}
