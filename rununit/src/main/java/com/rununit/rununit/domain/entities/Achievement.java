package com.rununit.rununit.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "achievements", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name", name = "UQ_ACHIEVEMENT_NAME")
})
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @Column(name = "icon_url", length = 500)
    private String iconUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "active", nullable = false)
    private Boolean active = true;


    @OneToMany(mappedBy = "achievement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AchievementCriteria> criteria;

    @OneToMany(mappedBy = "achievement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAchievement> userAchievements;
}