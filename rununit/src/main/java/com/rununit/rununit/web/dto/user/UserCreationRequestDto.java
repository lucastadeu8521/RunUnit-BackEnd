package com.rununit.rununit.web.dto.user;

import com.rununit.rununit.domain.enums.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserCreationRequestDto(

        @NotBlank(message = "The first name is required.")
        @Size(max = 100, message = "The first name cannot exceed 100 characters.")
        String name,

        @NotBlank(message = "The last name is required.")
        @Size(max = 150, message = "The last name cannot exceed 150 characters.")
        String lastName,

        @NotNull(message = "The birth date is required.")
        @Past(message = "The birth date must be in the past.")
        LocalDate birthDate,

        @NotNull(message = "Gender is required.")
        Gender gender,

        @Size(max = 50, message = "The timezone cannot exceed 50 characters.")
        String timezone,

        @Size(max = 10, message = "The locale cannot exceed 10 characters.")
        String locale,


        @NotBlank(message = "The email is required.")
        @Email(message = "The email must be in a valid format.")
        String email,

        @NotBlank(message = "The password is required.")
        @Size(min = 8, message = "The password must be at least 8 characters long.")
        String password
) {}