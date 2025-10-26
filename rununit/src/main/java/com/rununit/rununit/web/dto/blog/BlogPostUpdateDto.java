package com.rununit.rununit.web.dto.blog;

import com.rununit.rununit.domain.enums.PostStatus;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;

public record BlogPostUpdateDto(
        @Size(max = 255, message = "Title cannot exceed 255 characters")
        String title,

        String content,

        @Size(max = 500, message = "Excerpt cannot exceed 500 characters")
        String excerpt,

        @Size(max = 500, message = "Thumbnail URL cannot exceed 500 characters")
        String thumbnailUrl,

        PostStatus status,

        Instant publicationDate,

        List<Long> categoryIds
) {}