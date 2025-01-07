// File will be removed in the future
// This file is kept as a reference for the search feature of the application
package com.example.airbookingapp.air_booking_app.services.search;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.airbookingapp.air_booking_app.entity.Flight;

import org.springframework.data.jpa.domain.Specification;

public class FlightSpecifications {
    public static Specification<Flight> buildSpecification(SearchCriteria criteria) {
        return switch (criteria.getOperator().toUpperCase()) {
            case ":":
                yield (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(criteria.getKey()), parseValue(criteria.getKey(), criteria.getValue()));
            case "<":
                yield (root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThan(root.get(criteria.getKey()), (Comparable) parseValue(criteria.getKey(), criteria.getValue()));
            case ">":
                yield (root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThan(root.get(criteria.getKey()), (Comparable) parseValue(criteria.getKey(), criteria.getValue()));
            case "<=":
                yield (root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), (Comparable) parseValue(criteria.getKey(), criteria.getValue()));
            case ">=":
                yield (root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), (Comparable) parseValue(criteria.getKey(), criteria.getValue()));
            default:
                throw new IllegalArgumentException("Unsupported operator: " + criteria.getOperator());
        };
    }

    private static Object parseValue(String key, Object value) {
        if (key.equals("departureTime") || key.equals("returnTime")) {
            // Chuyển đổi String thành LocalDateTime
            return LocalDateTime.parse((String) value, DateTimeFormatter.ISO_DATE_TIME);
        }
        return value;
    }
}