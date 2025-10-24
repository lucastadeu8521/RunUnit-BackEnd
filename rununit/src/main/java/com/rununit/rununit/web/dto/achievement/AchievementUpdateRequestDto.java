package com.rununit.rununit.web.dto.achievement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record AchievementUpdateRequestDto(
        @Size(max = 100, message = "Name cannot exceed 100 characters.")
        String name,

        @Size(max = 255, message = "Description cannot exceed 255 characters.")
        String description,

        @Size(max = 500, message = "Icon URL cannot exceed 500 characters.")
        String iconUrl,

        Boolean active,

        // Criteria must be sent entirely for update to manage additions/deletions.
        @NotEmpty(message = "At least one criteria is required for an achievement.")
        @Valid
        List<AchievementCriteriaDto> criteria
) {}