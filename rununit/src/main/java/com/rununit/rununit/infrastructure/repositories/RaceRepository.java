package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RaceRepository extends JpaRepository<Race, UUID> {

    @Query("SELECT r FROM Race r " +
            "WHERE (:name IS NULL OR lower(r.name) LIKE lower(concat('%', :name, '%'))) " +
            "AND (:location IS NULL OR lower(r.location) LIKE lower(concat('%', :location, '%'))) " +
            "AND (:startDate IS NULL OR r.startTime >= :startDate) " +
            "AND (:endDate IS NULL OR r.startTime <= :endDate)")
    List<Race> findByFilters(
            @Param("name") String name,
            @Param("location") String location,
            @Param("startDate") ZonedDateTime startDate,
            @Param("endDate") ZonedDateTime endDate
    );


    List<Race> findByNameContainingIgnoreCase(String name);
    List<Race> findByCategoryContainingIgnoreCase(String category);
}
