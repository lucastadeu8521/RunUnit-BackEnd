package com.rununit.rununit.web.dto.race;

import com.rununit.rununit.domain.enums.Status;
import java.math.BigDecimal;
import java.time.Instant;

public record RaceResponseDto(
        Long id,
        String name,
        Instant raceDate,
        String venueName,
        String registrationUrl,
        String organizerContact,
        String city,
        String state,
        BigDecimal latitude,
        BigDecimal longitude,
        Integer maxParticipants,
        BigDecimal raceDistanceKm,
        Status status,
        Instant createdAt
) {}