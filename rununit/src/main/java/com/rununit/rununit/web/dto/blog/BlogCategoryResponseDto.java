package com.rununit.rununit.web.dto.blog;

import java.time.Instant;

public record BlogCategoryResponseDto(
        Long id,
        String name,
        String slug,
        String description,
        Instant createdAt,
        Boolean active
) {}