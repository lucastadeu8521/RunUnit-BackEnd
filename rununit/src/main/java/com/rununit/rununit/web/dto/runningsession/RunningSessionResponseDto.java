package com.rununit.rununit.web.dto.runningsession;

import java.math.BigDecimal;
import java.time.Instant;

public record RunningSessionResponseDto(
        Long id,
        Long userId,
        Long raceId,

        Instant startedAt,
        Integer elapsedSeconds,
        Integer distanceMeters,
        BigDecimal paceMinPerKm,

        Short avgHeartRate,
        Short peakHeartRate,
        BigDecimal elevationGainMeters,

        Instant createdAt,
        Boolean active
) {}