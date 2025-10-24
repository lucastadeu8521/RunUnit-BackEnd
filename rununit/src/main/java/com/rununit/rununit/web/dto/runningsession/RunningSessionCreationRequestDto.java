package com.rununit.rununit.web.dto.runningsession;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

public record RunningSessionCreationRequestDto(

        Long raceId,

        @NotNull(message = "The start time is required.")
        Instant startedAt,

        @NotNull(message = "Elapsed time in seconds is required.")
        @Min(value = 1, message = "Elapsed time must be greater than zero.")
        Integer elapsedSeconds,

        @NotNull(message = "Distance in meters is required.")
        @Min(value = 1, message = "Distance must be greater than zero.")
        Integer distanceMeters,

        @NotNull(message = "Pace is required.")
        @DecimalMin(value = "0.01", message = "Pace must be greater than zero.")
        BigDecimal paceMinPerKm,

        Short avgHeartRate,
        Short peakHeartRate,

        @DecimalMin(value = "0.00", message = "Elevation gain cannot be negative.")
        BigDecimal elevationGainMeters
) {}