package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.AchievementCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementCriteriaRepository extends JpaRepository<AchievementCriteria, Long> {

    List<AchievementCriteria> findByAchievementId(Long achievementId);

}