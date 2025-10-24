package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.UserAchievement;
import com.rununit.rununit.domain.entities.UserAchievementId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, UserAchievementId> {

    List<UserAchievement> findByIdUserId(Long userId);

    Optional<UserAchievement> findByIdUserIdAndIdAchievementId(Long userId, Long achievementId);
}