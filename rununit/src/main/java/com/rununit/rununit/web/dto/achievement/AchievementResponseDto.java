package com.rununit.rununit.web.dto.achievement;

import java.time.Instant;
import java.util.List;

public record AchievementResponseDto(
        Long id,
        String name,
        String description,
        String iconUrl,
        Instant createdAt,
        Boolean active,
        List<AchievementCriteriaDto> criteria
) {}