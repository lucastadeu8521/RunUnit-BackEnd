package com.rununit.rununit.web.dto.userrace;

import com.rununit.rununit.domain.enums.UserRaceTag;

public record UserRaceUpdateRequestDto(
        UserRaceTag tag,
        Boolean active
) {}