package com.rununit.rununit.web.dto.blog;

public record UserSimpleResponseDto(
        Long id,
        String name,
        String lastName,
        String profilePictureUrl
) {}