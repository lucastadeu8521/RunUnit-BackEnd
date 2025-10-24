package com.rununit.rununit.web.dto.membershiptype;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record MembershipTypeUpdateRequestDto(
        @Size(max = 50, message = "Name cannot exceed 50 characters.")
        String name,

        @DecimalMin(value = "0.00", message = "Price cannot be negative.")
        BigDecimal monthlyPrice,

        @Size(max = 255, message = "Description cannot exceed 255 characters.")
        String description
) {}