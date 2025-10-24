package com.rununit.rununit.web.controllers;

import com.rununit.rununit.domain.services.UserAchievementService;
import com.rununit.rununit.web.dto.userachievement.UserAchievementResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserAchievementController {

    private final UserAchievementService userAchievementService;

    public UserAchievementController(UserAchievementService userAchievementService) {
        this.userAchievementService = userAchievementService;
    }

    private Long getAuthenticatedUserId() {
        return 1L;
    }

    @GetMapping("/my/achievements")
    public List<UserAchievementResponseDto> getMyAchievements() {
        Long userId = getAuthenticatedUserId();
        return userAchievementService.getUserAchievements(userId);
    }

    @GetMapping("/my/achievements/{achievementId}")
    public ResponseEntity<UserAchievementResponseDto> getMySpecificAchievement(@PathVariable Long achievementId) {
        Long userId = getAuthenticatedUserId();
        UserAchievementResponseDto responseDto = userAchievementService.getUserAchievement(userId, achievementId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/admin/user-achievements/{userId}/{achievementId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeAchievement(
            @PathVariable Long userId,
            @PathVariable Long achievementId) {

        userAchievementService.revokeAchievement(userId, achievementId);
    }

}