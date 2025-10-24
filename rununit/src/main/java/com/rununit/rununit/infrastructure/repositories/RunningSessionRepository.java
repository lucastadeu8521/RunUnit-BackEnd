package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.RunningSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningSessionRepository extends JpaRepository<RunningSession, Long> {

    List<RunningSession> findByUserId(Long userId);
    List<RunningSession> findByRaceId(Long raceId);
}
