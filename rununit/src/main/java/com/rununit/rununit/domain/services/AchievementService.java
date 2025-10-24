package com.rununit.rununit.domain.services;

import com.rununit.rununit.domain.entities.Achievement;
import com.rununit.rununit.domain.entities.AchievementCriteria;
import com.rununit.rununit.infrastructure.repositories.AchievementCriteriaRepository;
import com.rununit.rununit.infrastructure.repositories.AchievementRepository;
import com.rununit.rununit.web.dto.achievement.AchievementCreationRequestDto;
import com.rununit.rununit.web.dto.achievement.AchievementCriteriaDto;
import com.rununit.rununit.web.dto.achievement.AchievementResponseDto;
import com.rununit.rununit.web.dto.achievement.AchievementUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final AchievementCriteriaRepository criteriaRepository;

    public AchievementService(AchievementRepository achievementRepository, AchievementCriteriaRepository criteriaRepository) {
        this.achievementRepository = achievementRepository;
        this.criteriaRepository = criteriaRepository;
    }

    private AchievementCriteriaDto toCriteriaDto(AchievementCriteria criteria) {
        return new AchievementCriteriaDto(
                criteria.getId(),
                criteria.getMetricType(),
                criteria.getRequiredValue(),
                criteria.getComparisonOperator()
        );
    }

    private AchievementResponseDto toResponseDto(Achievement achievement) {
        List<AchievementCriteriaDto> criteriaDtos = achievement.getCriteria().stream()
                .map(this::toCriteriaDto)
                .collect(Collectors.toList());

        return new AchievementResponseDto(
                achievement.getId(),
                achievement.getName(),
                achievement.getDescription(),
                achievement.getIconUrl(),
                achievement.getCreatedAt(),
                achievement.getActive(),
                criteriaDtos
        );
    }

    @Transactional
    public AchievementResponseDto createAchievement(AchievementCreationRequestDto requestDto) {
        if (achievementRepository.existsByNameIgnoreCase(requestDto.name())) {
            throw new RuntimeException("Achievement name already exists: " + requestDto.name());
        }

        Achievement achievement = new Achievement();
        achievement.setName(requestDto.name());
        achievement.setDescription(requestDto.description());
        achievement.setIconUrl(requestDto.iconUrl());
        achievement.setActive(true);
        achievement.setCriteria(new ArrayList<>()); // Initialize list

        List<AchievementCriteria> criteriaEntities = requestDto.criteria().stream()
                .map(dto -> {
                    AchievementCriteria criteria = new AchievementCriteria();
                    criteria.setAchievement(achievement); // Set the back-reference
                    criteria.setMetricType(dto.metricType());
                    criteria.setRequiredValue(dto.requiredValue());
                    criteria.setComparisonOperator(dto.comparisonOperator());
                    return criteria;
                })
                .collect(Collectors.toList());

        achievement.getCriteria().addAll(criteriaEntities);

        Achievement savedAchievement = achievementRepository.save(achievement);
        return toResponseDto(savedAchievement);
    }

    public List<AchievementResponseDto> findAllAchievements() {
        return achievementRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public AchievementResponseDto findAchievementById(Long id) {
        return achievementRepository.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Achievement not found with ID: " + id));
    }

    @Transactional
    public AchievementResponseDto updateAchievement(Long id, AchievementUpdateRequestDto requestDto) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Achievement not found with ID: " + id));

        if (requestDto.name() != null) {
            if (!achievement.getName().equalsIgnoreCase(requestDto.name()) && achievementRepository.existsByNameIgnoreCase(requestDto.name())) {
                throw new RuntimeException("Achievement name already exists: " + requestDto.name());
            }
            achievement.setName(requestDto.name());
        }
        if (requestDto.description() != null) achievement.setDescription(requestDto.description());
        if (requestDto.iconUrl() != null) achievement.setIconUrl(requestDto.iconUrl());
        if (requestDto.active() != null) achievement.setActive(requestDto.active());

        List<AchievementCriteria> currentCriteria = achievement.getCriteria();
        List<AchievementCriteria> updatedCriteria = new ArrayList<>();

        for (AchievementCriteriaDto dto : requestDto.criteria()) {
            if (dto.id() != null) {
                AchievementCriteria existingCriteria = currentCriteria.stream()
                        .filter(c -> c.getId().equals(dto.id()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Criteria ID not found: " + dto.id()));

                existingCriteria.setMetricType(dto.metricType());
                existingCriteria.setRequiredValue(dto.requiredValue());
                existingCriteria.setComparisonOperator(dto.comparisonOperator());
                updatedCriteria.add(existingCriteria);
            } else {
                AchievementCriteria newCriteria = new AchievementCriteria();
                newCriteria.setAchievement(achievement);
                newCriteria.setMetricType(dto.metricType());
                newCriteria.setRequiredValue(dto.requiredValue());
                newCriteria.setComparisonOperator(dto.comparisonOperator());
                updatedCriteria.add(newCriteria);
            }
        }

        achievement.setCriteria(updatedCriteria);

        Achievement savedAchievement = achievementRepository.save(achievement);
        return toResponseDto(savedAchievement);
    }

    @Transactional
    public void deleteAchievement(Long id) {
        if (!achievementRepository.existsById(id)) {
            throw new RuntimeException("Achievement not found with ID: " + id);
        }
        achievementRepository.deleteById(id);
    }
}