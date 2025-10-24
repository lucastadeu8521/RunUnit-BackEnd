package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    boolean existsByNameIgnoreCase(String name);
}