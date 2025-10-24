package com.rununit.rununit.web.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequestDto(
        @NotBlank(message = "The current password is required.")
        String currentPassword,

        @NotBlank(message = "The new password is required.")
        @Size(min = 8, message = "The new password must be at least 8 characters long.")
        String newPassword,

        @NotBlank(message = "The password confirmation is required.")
        String newPasswordConfirmation
) {}