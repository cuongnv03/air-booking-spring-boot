package com.example.airbookingapp.air_booking_app.repositories;

import com.example.airbookingapp.air_booking_app.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String>, JpaSpecificationExecutor<Flight> {
    public abstract Page<Flight> findAll(Specification<Flight> specification, Pageable pageable);
}
