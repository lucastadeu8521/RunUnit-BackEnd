package com.rununit.rununit.repositories;

import com.rununit.rununit.entities.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RaceRepository extends JpaRepository<Race, UUID> {
}