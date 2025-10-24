package com.rununit.rununit.web.dto.userrace;

import com.rununit.rununit.domain.enums.UserRaceTag;
import jakarta.validation.constraints.NotNull;

public record UserRaceCreationRequestDto(
        @NotNull(message = "Race ID is required.")
        Long raceId,

        @NotNull(message = "User tag in race is mandatory.")
        UserRaceTag tag
) {}