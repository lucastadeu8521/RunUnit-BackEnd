package com.rununit.rununit.web.dto.userrace;

import com.rununit.rununit.domain.enums.UserRaceTag;
import java.time.Instant;

public record UserRaceResponseDto(
        Long userId,
        Long raceId,
        UserRaceTag tag,
        Instant createdAt,
        Boolean active
) {}