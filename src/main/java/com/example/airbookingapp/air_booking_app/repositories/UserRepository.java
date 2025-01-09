package com.example.airbookingapp.air_booking_app.repositories;

import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Users;
import com.example.airbookingapp.air_booking_app.jooq.tables.records.UsersRecord;
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

    public Users save(Users user) {
        UsersRecord record = dsl.newRecord(Tables.USERS, user);
        record.store();
        return record.into(Users.class);
    }

    public Optional<Users> findByUsername(String username) {
        return dsl.selectFrom(Tables.USERS)
                .where(Tables.USERS.USERNAME.eq(username))
                .fetchOptionalInto(Users.class);
    }

    public boolean existsByUsername(String username) {
        return dsl.fetchExists(
                dsl.selectFrom(Tables.USERS)
                        .where(Tables.USERS.USERNAME.eq(username))
        );
    }
}
