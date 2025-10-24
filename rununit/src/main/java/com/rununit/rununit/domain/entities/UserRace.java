package com.rununit.rununit.domain.entities;

import com.rununit.rununit.domain.enums.UserRaceTag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "user_races")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRace {

    @EmbeddedId
    private UserRaceId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("raceId")
    @JoinColumn(name = "race_id", nullable = false)
    private Race race;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag", nullable = false, length = 15)
    private UserRaceTag tag;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}