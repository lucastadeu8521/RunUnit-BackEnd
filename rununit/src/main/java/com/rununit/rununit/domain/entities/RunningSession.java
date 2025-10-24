package com.rununit.rununit.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "running_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunningSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "race_id")
    private Race race;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "elapsed_seconds", nullable = false)
    private Integer elapsedSeconds;

    @Column(name = "distance_meters", nullable = false)
    private Integer distanceMeters;

    @Column(name = "avg_heart_rate")
    private Short avgHeartRate;

    @Column(name = "peak_heart_rate")
    private Short peakHeartRate;

    @Column(name = "elevation_gain_meters", precision = 6, scale = 2)
    private BigDecimal elevationGainMeters;

    @Column(name = "pace_min_per_km", nullable = false, precision = 4, scale = 2)
    private BigDecimal paceMinPerKm;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}