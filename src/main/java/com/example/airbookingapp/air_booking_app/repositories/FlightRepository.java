package com.example.airbookingapp.air_booking_app.repositories;

import com.example.airbookingapp.air_booking_app.data.request.SearchFlightRequest;
import com.example.airbookingapp.air_booking_app.jooq.Tables;
import com.example.airbookingapp.air_booking_app.jooq.tables.pojos.Flight;
import com.example.airbookingapp.air_booking_app.jooq.tables.records.FlightRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class FlightRepository {

    private final DSLContext dsl;

    public FlightRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    // Save a new flight
    public Flight save(Flight flight) {
        FlightRecord record = dsl.newRecord(Tables.FLIGHT);
        record.from(flight); // Map POJO to Record
        record.store();      // Save to DB
        return record.into(Flight.class); // Convert back to POJO
    }

    // Find a flight by ID
    public Flight findById(String id) {
        FlightRecord record = dsl
                .selectFrom(Tables.FLIGHT)
                .where(Tables.FLIGHT.FLIGHT_ID.eq(id))
                .fetchOne();
        assert record != null;
        return record.into(Flight.class); // Convert Record to POJO
    }

    // Find all flights with pagination
    public List<Flight> findAll() {
        return dsl.selectFrom(Tables.FLIGHT)
                .fetchInto(Flight.class); // Convert fetched records to POJOs
    }

    // Delete a flight by ID
    public boolean deleteById(String flightId) {
        int rowsAffected = dsl.deleteFrom(Tables.FLIGHT)
                .where(Tables.FLIGHT.FLIGHT_ID.eq(flightId))
                .execute();
        return rowsAffected > 0; // Return true if a record was deleted
    }

    // Update an existing flight
    public Flight update(String flightId, Flight flight) {
        FlightRecord record = dsl.newRecord(Tables.FLIGHT);
        record.from(flight); // Map POJO to Record
        int rowsAffected = dsl.update(Tables.FLIGHT)
                .set(record)
                .where(Tables.FLIGHT.FLIGHT_ID.eq(flightId))
                .execute();

        if (rowsAffected > 0) {
            return findById(flightId); // Return the updated flight as a POJO
        } else {
            throw new IllegalStateException("Flight with ID " + flightId + " not found.");
        }
    }

    // Count the total number of flights (for pagination purposes)
    public long count() {
        return dsl.fetchCount(Tables.FLIGHT); // Count the total number of rows in the table
    }

    // Search for flights based on filters
    public List<Flight> search(List<SearchFlightRequest> filters) {
        Condition condition = buildCondition(filters); // Build jOOQ condition from filters
        return dsl.selectFrom(Tables.FLIGHT)
                .where(condition)
                .fetchInto(Flight.class); // Fetch results into POJOs
    }

    // Build a jOOQ condition from a list of filters
    public long countSearch(List<SearchFlightRequest> filters) {
        Condition condition = buildCondition(filters); // Build jOOQ condition from filters
        return dsl.fetchCount(dsl.selectFrom(Tables.FLIGHT).where(condition));
    }

    // Build jOOQ Condition from Filters
    private Condition buildCondition(List<SearchFlightRequest> filters) {
        if (filters == null || filters.isEmpty()) {
            return DSL.noCondition(); // Return no condition if filters are empty
        }

        Condition condition = createCondition(filters.removeFirst());
        for (SearchFlightRequest filter : filters) {
            condition = condition.and(createCondition(filter));
        }
        return condition;
    }

    // Create jOOQ Condition from Single Filter
    private Condition createCondition(SearchFlightRequest filter) {
        // Determine the field and its expected type dynamically
        Field<Object> field = Tables.FLIGHT.field(filter.getKey(), Object.class); // Generic Field
        if (field == null) {
            throw new IllegalArgumentException("Invalid field key: " + filter.getKey());
        }
        Object castedValue = castToRequiredType(filter.getKey(), filter.getValue());

        return switch (filter.getOperator()) {
            case ":" -> field.eq(castedValue); // Equal condition
            case "<" -> field.lt((Comparable<?>) castedValue); // Less than condition
            case ">" -> field.gt((Comparable<?>) castedValue); // Greater than condition
            case "<=" -> field.le((Comparable<?>) castedValue); // Less than or equal condition
            case ">=" -> field.ge((Comparable<?>) castedValue); // Greater than or equal condition
            default -> throw new IllegalArgumentException("Unsupported operator: " + filter.getOperator());
        };
    }

    // Cast Values to Required Type for jOOQ Condition
    private Object castToRequiredType(String key, String value) {
        if (value == null) return null;
        return switch (key) {
            case "departure_time", "return_time" ->
                    LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME); // Cast to LocalDateTime
            case "duration" -> Integer.parseInt(value); // Example: Cast to Integer if needed
            default -> value; // Assume string for other fields
        };
    }
}
