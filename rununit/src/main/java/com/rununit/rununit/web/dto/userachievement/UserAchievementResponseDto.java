package com.rununit.rununit.web.dto.userachievement;

import java.math.BigDecimal;
import java.time.Instant;

public record UserAchievementResponseDto(
        Long userId,
        Long achievementId,
        String achievementName,
        Instant achievedAt,
        BigDecimal achievedValue
) {}