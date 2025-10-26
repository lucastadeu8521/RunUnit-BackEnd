package com.rununit.rununit.web.dto.blog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BlogPostCreationDto(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title cannot exceed 255 characters")
        String title,

        @NotBlank(message = "Slug is required")
        @Size(max = 255, message = "Slug cannot exceed 255 characters")
        String slug,

        @NotBlank(message = "Content is required")
        String content,

        @Size(max = 500, message = "Excerpt cannot exceed 500 characters")
        String excerpt,

        @Size(max = 500, message = "Thumbnail URL cannot exceed 500 characters")
        String thumbnailUrl,

        @NotNull(message = "Author ID is required")
        Long authorId,

        List<Long> categoryIds
) {}