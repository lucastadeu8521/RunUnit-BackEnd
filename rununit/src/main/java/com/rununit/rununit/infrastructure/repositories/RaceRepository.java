package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;


@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {

    @Query("SELECT r FROM Race r " +
            "WHERE (:name IS NULL OR lower(r.name) LIKE lower(concat('%', :name, '%'))) " +
            "AND (:location IS NULL OR lower(r.city) LIKE lower(concat('%', :location, '%')) OR lower(r.state) LIKE lower(concat('%', :location, '%'))) " +
            "AND (:startDate IS NULL OR r.raceDate >= :startDate) " +
            "AND (:endDate IS NULL OR r.raceDate <= :endDate)")
    List<Race> findByFilters(
            @Param("name") String name,
            @Param("location") String location,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate
    );

    List<Race> findByNameContainingIgnoreCase(String name);

    List<Race> findByCityContainingIgnoreCase(String city);
}