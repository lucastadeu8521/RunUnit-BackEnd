package com.rununit.rununit.web.dto.membershiptype;

import java.math.BigDecimal;
import java.time.Instant;

public record MembershipTypeResponseDto(
        Long id,
        String name,
        BigDecimal monthlyPrice,
        String description,
        Instant createdAt
) {}