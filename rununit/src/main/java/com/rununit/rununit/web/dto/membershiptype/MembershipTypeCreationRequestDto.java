package com.rununit.rununit.web.dto.membershiptype;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record MembershipTypeCreationRequestDto(
        @NotBlank(message = "The name is required.")
        @Size(max = 50, message = "Name cannot exceed 50 characters.")
        String name,

        @NotNull(message = "The monthly price is required.")
        @DecimalMin(value = "0.00", message = "Price cannot be negative.")
        BigDecimal monthlyPrice,

        @Size(max = 255, message = "Description cannot exceed 255 characters.")
        String description
) {}