package com.rununit.rununit.web.controllers;

import com.rununit.rununit.domain.services.AchievementService;
import com.rununit.rununit.web.dto.achievement.AchievementCreationRequestDto;
import com.rununit.rununit.web.dto.achievement.AchievementResponseDto;
import com.rununit.rununit.web.dto.achievement.AchievementUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admin/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @PostMapping
    public ResponseEntity<AchievementResponseDto> createAchievement(
            @Valid @RequestBody AchievementCreationRequestDto requestDto) {

        AchievementResponseDto responseDto = achievementService.createAchievement(requestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.id())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping
    public List<AchievementResponseDto> getAllAchievements() {
        return achievementService.findAllAchievements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementResponseDto> getAchievementById(@PathVariable Long id) {
        AchievementResponseDto responseDto = achievementService.findAchievementById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AchievementResponseDto> updateAchievement(
            @PathVariable Long id,
            @Valid @RequestBody AchievementUpdateRequestDto requestDto) {

        AchievementResponseDto responseDto = achievementService.updateAchievement(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        achievementService.deleteAchievement(id);
        return ResponseEntity.noContent().build();
    }
}