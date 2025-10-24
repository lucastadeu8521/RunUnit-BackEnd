package com.rununit.rununit.web.dto.userbiometrics;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserBiometricsCreationRequestDto(
        @NotBlank(message = "Facial embedding data is required.")
        @Size(min = 10, message = "Embedding data is too short.")
        String facialEmbeddingBase64
) {}