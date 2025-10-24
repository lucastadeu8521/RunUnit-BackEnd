package com.rununit.rununit.web.dto.achievement;

import com.rununit.rununit.domain.enums.ComparisonOperator;
import com.rununit.rununit.domain.enums.MetricType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AchievementCriteriaDto(
        Long id,

        @NotNull(message = "Metric type is required.")
        MetricType metricType,

        @NotNull(message = "Required value is mandatory.")
        @DecimalMin(value = "0.01", message = "Required value must be greater than zero.")
        BigDecimal requiredValue,

        @NotNull(message = "Comparison operator is required.")
        ComparisonOperator comparisonOperator
) {}