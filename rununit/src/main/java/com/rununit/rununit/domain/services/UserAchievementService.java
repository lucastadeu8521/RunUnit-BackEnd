package com.rununit.rununit.domain.services;

import com.rununit.rununit.domain.entities.Achievement;
import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.domain.entities.UserAchievement;
import com.rununit.rununit.domain.entities.UserAchievementId;
import com.rununit.rununit.infrastructure.repositories.AchievementRepository;
import com.rununit.rununit.infrastructure.repositories.UserAchievementRepository;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import com.rununit.rununit.web.dto.userachievement.AchievementGrantRequestDto;
import com.rununit.rununit.web.dto.userachievement.UserAchievementResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAchievementService {

    private final UserAchievementRepository userAchievementRepository;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;

    public UserAchievementService(UserAchievementRepository userAchievementRepository, UserRepository userRepository, AchievementRepository achievementRepository) {
        this.userAchievementRepository = userAchievementRepository;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
    }

    private UserAchievementResponseDto toResponseDto(UserAchievement userAchievement) {
        return new UserAchievementResponseDto(
                userAchievement.getUser().getId(),
                userAchievement.getAchievement().getId(),
                userAchievement.getAchievement().getName(),
                userAchievement.getAchievedAt(),
                userAchievement.getAchievedValue()
        );
    }

    @Transactional
    public UserAchievementResponseDto grantAchievement(Long userId, AchievementGrantRequestDto requestDto) {
        Long achievementId = requestDto.achievementId();

        if (userAchievementRepository.findByIdUserIdAndIdAchievementId(userId, achievementId).isPresent()) {
            throw new RuntimeException("Achievement ID " + achievementId + " already earned by User ID " + userId);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new RuntimeException("Achievement not found with ID: " + achievementId));

        UserAchievementId id = new UserAchievementId(userId, achievementId);

        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setId(id);
        userAchievement.setUser(user);
        userAchievement.setAchievement(achievement);
        userAchievement.setAchievedAt(requestDto.achievedAt());
        userAchievement.setAchievedValue(requestDto.achievedValue());

        UserAchievement savedRecord = userAchievementRepository.save(userAchievement);
        return toResponseDto(savedRecord);
    }

    public List<UserAchievementResponseDto> getUserAchievements(Long userId) {
        // Validation for user existence is good practice
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        return userAchievementRepository.findByIdUserId(userId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public UserAchievementResponseDto getUserAchievement(Long userId, Long achievementId) {
        return userAchievementRepository.findByIdUserIdAndIdAchievementId(userId, achievementId)
                .map(this::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Achievement not earned by User ID: " + userId));
    }

    @Transactional
    public void revokeAchievement(Long userId, Long achievementId) {
        UserAchievement userAchievement = userAchievementRepository.findByIdUserIdAndIdAchievementId(userId, achievementId)
                .orElseThrow(() -> new RuntimeException("Achievement not earned by User ID: " + userId));

        userAchievementRepository.delete(userAchievement);
    }
}