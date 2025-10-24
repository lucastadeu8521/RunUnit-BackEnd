package com.rununit.rununit.web.dto.userbiometrics;

public record UserBiometricsUpdateRequestDto(
        String facialEmbeddingBase64,
        Boolean biometricActive
) {}