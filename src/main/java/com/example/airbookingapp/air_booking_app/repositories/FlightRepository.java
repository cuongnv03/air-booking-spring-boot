package com.example.airbookingapp.air_booking_app.repositories;

import com.example.airbookingapp.air_booking_app.data.request.SearchFlightRequest;
import com.example.airbookingapp.air_booking_app.jooq.Tables;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Flight;
import com.example.airbookingapp.air_booking_app.jooq.tables.records.FlightRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FlightRepository {

    private final DSLContext dsl;

    // Lưu một chuyến bay mới
    public Flight save(Flight flight) {
        FlightRecord record = dsl.newRecord(Tables.FLIGHT);
        record.from(flight); // Map POJO to Record
        record.store();      // Lưu vào cơ sở dữ liệu
        // Retrieve the saved flight with the generated flightId
        return dsl.selectFrom(Tables.FLIGHT)
                .where(Tables.FLIGHT.ID.eq(record.getId()))
                .fetchOneInto(Flight.class);
    }

    // Tìm chuyến bay theo flightId
    public Flight findByFlightId(String flightId) {
        return dsl.selectFrom(Tables.FLIGHT)
                .where(Tables.FLIGHT.FLIGHT_ID.eq(flightId))
                .fetchOneInto(Flight.class);
    }

    // Tìm tất cả các chuyến bay
    public List<Flight> findAll(int page, int size) {
        return dsl.selectFrom(Tables.FLIGHT)
                .orderBy(Tables.FLIGHT.FLIGHT_ID.asc())
                .limit(size)
                .offset(page * size)
                .fetchInto(Flight.class);
    }

    // Xóa một chuyến bay theo flightId
    public boolean deleteByFlightId(String flightId) {
        int rowsAffected = dsl.deleteFrom(Tables.FLIGHT)
                .where(Tables.FLIGHT.FLIGHT_ID.eq(flightId))
                .execute();
        return rowsAffected > 0;
    }

    // Cập nhật thông tin chuyến bay
    public Flight update(String flightId, Flight flight) {
        FlightRecord record = dsl.newRecord(Tables.FLIGHT);
        record.from(flight);
        int rowsAffected = dsl.update(Tables.FLIGHT)
                .set(record)
                .where(Tables.FLIGHT.FLIGHT_ID.eq(flightId))
                .execute();

        if (rowsAffected > 0) {
            return findByFlightId(flightId);
        } else {
            throw new IllegalStateException("Không tìm thấy chuyến bay với flightId: " + flightId);
        }
    }

    // Count the total number of flights (for pagination purposes)
    public long count() {
        return dsl.fetchCount(Tables.FLIGHT); // Count the total number of rows in the table
    }

    // Tìm kiếm chuyến bay theo bộ lọc
    public List<Flight> search(List<SearchFlightRequest> filters, int page, int size) {
        Condition condition = buildCondition(filters);
        return dsl.selectFrom(Tables.FLIGHT)
                .where(condition)
                .limit(size)
                .offset(page * size)
                .fetchInto(Flight.class);
    }

    public long countSearch(List<SearchFlightRequest> filters) {
        Condition condition = buildCondition(filters); // Build jOOQ condition from filters
        return dsl.fetchCount(dsl.selectFrom(Tables.FLIGHT).where(condition));
    }

    // Xây dựng điều kiện tìm kiếm từ bộ lọc
    private Condition buildCondition(List<SearchFlightRequest> filters) {
        if (filters == null || filters.isEmpty()) {
            return DSL.noCondition();
        }

        Condition condition = createCondition(filters.removeFirst());
        for (SearchFlightRequest filter : filters) {
            condition = condition.and(createCondition(filter));
        }
        return condition;
    }

    // Tạo điều kiện tìm kiếm từ một bộ lọc
    private Condition createCondition(SearchFlightRequest filter) {
        Field<Object> field = Tables.FLIGHT.field(filter.getKey(), Object.class);
        if (field == null) {
            throw new IllegalArgumentException("Invalid field key: " + filter.getKey());
        }
        Object value = castToRequiredType(filter.getKey(), filter.getValue());

        return switch (filter.getOperator()) {
            case "=" -> field.eq(value);
            case "<" -> field.lt(value);
            case ">" -> field.gt(value);
            case "<=" -> field.le(value);
            case ">=" -> field.ge(value);
            default -> throw new IllegalArgumentException("Unsupported operator: " + filter.getOperator());
        };
    }

    // Chuyển kiểu dữ liệu theo yêu cầu
    private Object castToRequiredType(String key, String value) {
        if (value == null) return null;
        return switch (key) {
            case "scheduled_time" -> LocalDateTime.parse(value);
            case "price_e", "price_b" -> Long.parseLong(value);
            default -> value;
        };
    }
}
