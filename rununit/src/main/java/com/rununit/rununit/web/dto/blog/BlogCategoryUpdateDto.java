package com.rununit.rununit.web.dto.blog;

import jakarta.validation.constraints.Size;
import java.lang.Boolean;

public record BlogCategoryUpdateDto(
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,

        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description,

        Boolean active
) {}