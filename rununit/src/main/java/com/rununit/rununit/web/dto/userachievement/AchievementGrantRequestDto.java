package com.rununit.rununit.web.dto.userachievement;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

public record AchievementGrantRequestDto(
        @NotNull Long achievementId,
        @NotNull BigDecimal achievedValue,
        @NotNull Instant achievedAt
) {}