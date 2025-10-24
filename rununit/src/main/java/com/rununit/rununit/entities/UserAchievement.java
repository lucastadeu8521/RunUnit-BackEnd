package com.rununit.rununit.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_achievements")
public class UserAchievement {


    @EmbeddedId
    private UserAchievementId id;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_USERACHIEVEMENT_USER_ID"))
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("achievementId")
    @JoinColumn(name = "achievement_id", nullable = false, foreignKey = @ForeignKey(name = "FK_USERACHIEVEMENT_ACHIEVEMENT_ID", onDelete = ConstraintMode.RESTRICT))
    private Achievement achievement;


    @Column(name = "achieved_at", nullable = false)
    private LocalDateTime achievedAt;


    @Column(name = "achieved_value", precision = 10, scale = 2)
    private BigDecimal achievedValue;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


}
