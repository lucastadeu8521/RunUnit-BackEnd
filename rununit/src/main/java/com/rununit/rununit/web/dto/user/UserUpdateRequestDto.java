package com.rununit.rununit.web.dto.user;

import com.rununit.rununit.domain.enums.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserUpdateRequestDto(
        @Size(max = 100, message = "The first name cannot exceed 100 characters.")
        String name,

        @Size(max = 150, message = "The last name cannot exceed 150 characters.")
        String lastName,

        @Past(message = "The birth date must be in the past.")
        LocalDate birthDate,

        Gender gender,

        @Size(max = 50, message = "The timezone cannot exceed 50 characters.")
        String timezone,

        @Size(max = 10, message = "The locale cannot exceed 10 characters.")
        String locale,

        @Size(max = 500, message = "The profile picture URL cannot exceed 500 characters.")
        String profilePictureUrl
) {}