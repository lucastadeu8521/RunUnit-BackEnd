package com.rununit.rununit.web.dto.blog;

import com.rununit.rununit.domain.enums.PostStatus;
import com.rununit.rununit.web.dto.user.UserResponseDto; // Usaremos um DTO simples para o autor

import java.time.Instant;
import java.util.List;

public record BlogPostResponseDto(
        Long id,
        String title,
        String slug,
        String excerpt,
        String thumbnailUrl,
        String content,
        PostStatus status,
        Instant publicationDate,
        Instant createdAt,
        Instant updatedAt,

        UserSimpleResponseDto author,
        List<BlogCategoryResponseDto> categories
) {}