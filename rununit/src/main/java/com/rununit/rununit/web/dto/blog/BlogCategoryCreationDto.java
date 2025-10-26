package com.rununit.rununit.web.dto.blog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BlogCategoryCreationDto(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,

        @NotBlank(message = "Slug is required")
        @Size(max = 100, message = "Slug cannot exceed 100 characters")
        String slug,

        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description
) {}