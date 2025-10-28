package com.rununit.rununit.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@ToString
@Builder // ADICIONADO: Necessário para o Login.builder()
@NoArgsConstructor // ADICIONADO: Necessário com @Builder e @AllArgsConstructor
@AllArgsConstructor // ADICIONADO: Necessário com @Builder e @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "logins")
public class Login {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "password_changed_at")
    private Instant passwordChangedAt;

    @Default // ADICIONADO: Para resolver warnings do @Builder
    @Column(name = "failed_login_attempts", nullable = false)
    private Short failedLoginAttempts = 0;

    @Column(name = "lockout_until")
    private Instant lockoutUntil;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Default // ADICIONADO: Para resolver warnings do @Builder
    @Column(name = "active", nullable = false)
    private Boolean active = true;
}