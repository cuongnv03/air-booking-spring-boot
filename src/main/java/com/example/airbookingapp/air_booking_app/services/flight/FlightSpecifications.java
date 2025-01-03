package com.example.airbookingapp.air_booking_app.services.flight;

import com.example.airbookingapp.air_booking_app.domain.Flight;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class FlightSpecifications {

    public static Specification<Flight> hasDeparturePoint(String departurePoint) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("departurePoint"), departurePoint);
    }

    public static Specification<Flight> hasDestinationPoint(String destinationPoint) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("destinationPoint"), destinationPoint);
    }

    public static Specification<Flight> hasDepartureTime(LocalDateTime departureTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("departureTime"), departureTime);
    }

    public static Specification<Flight> hasReturnTime(LocalDateTime returnTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("returnTime"), returnTime);
    }
}

