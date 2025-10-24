package com.rununit.rununit.domain.entities;

import com.rununit.rununit.domain.enums.ComparisonOperator;
import com.rununit.rununit.domain.enums.MetricType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "achievement_criteria")
public class AchievementCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type", nullable = false)
    private MetricType metricType;

    @Column(name = "required_value", precision = 10, scale = 2, nullable = false)
    private BigDecimal requiredValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "comparison_operator", nullable = false)
    private ComparisonOperator comparisonOperator;

}