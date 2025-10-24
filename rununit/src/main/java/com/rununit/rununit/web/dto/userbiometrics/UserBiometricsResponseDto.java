package com.rununit.rununit.web.dto.userbiometrics;

import java.time.Instant;

public record UserBiometricsResponseDto(
        Long userId,
        Boolean biometricActive,
        Instant registeredAt
) {}