package com.rununit.rununit.web.dto.runningsession;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.Instant;

public record RunningSessionUpdateRequestDto(
        Long raceId,

        Instant startedAt,

        @Min(value = 1, message = "Elapsed time must be greater than zero.")
        Integer elapsedSeconds,

        @Min(value = 1, message = "Distance must be greater than zero.")
        Integer distanceMeters,

        Short avgHeartRate,
        Short peakHeartRate,

        @DecimalMin(value = "0.00", message = "Elevation gain cannot be negative.")
        BigDecimal elevationGainMeters,

        @DecimalMin(value = "0.01", message = "Pace must be greater than zero.")
        BigDecimal paceMinPerKm,

        Boolean active
) {}