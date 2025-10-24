package com.rununit.rununit.web.dto.user;

import com.rununit.rununit.domain.enums.UserRole;
import com.rununit.rununit.domain.enums.Gender;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record UserResponseDto(
        Long id,
        String name,
        String lastName,
        String email,
        UserRole userRole,
        String profilePictureUrl,
        LocalDate birthDate,
        Gender gender,
        String timezone,
        String locale,


        BigDecimal totalRunningDistance,
        Long totalRunningTime,


        boolean active,
        Instant createdAt
) {}