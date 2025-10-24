package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.UserRace;
import com.rununit.rununit.domain.entities.UserRaceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRaceRepository extends JpaRepository<UserRace, UserRaceId> {

    List<UserRace> findByIdUserId(Long userId);

    List<UserRace> findByIdUserIdAndActiveTrue(Long userId);

    List<UserRace> findByIdRaceId(Long raceId);

    Optional<UserRace> findByIdUserIdAndIdRaceId(Long userId, Long raceId);
}