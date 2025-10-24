package com.rununit.rununit.web.dto.achievement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record AchievementCreationRequestDto(
        @NotBlank(message = "Name is required.")
        @Size(max = 100, message = "Name cannot exceed 100 characters.")
        String name,

        @NotBlank(message = "Description is required.")
        @Size(max = 255, message = "Description cannot exceed 255 characters.")
        String description,

        @Size(max = 500, message = "Icon URL cannot exceed 500 characters.")
        String iconUrl,

        @NotEmpty(message = "At least one criteria is required for an achievement.")
        @Valid
        List<AchievementCriteriaDto> criteria
) {}