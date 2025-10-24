package com.rununit.rununit.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "user_biometrics")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class UserBiometrics {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name = "facial_embedding", nullable = false)
    private byte[] facialEmbedding;

    @Column(name = "biometric_active", nullable = false)
    private Boolean biometricActive = true;

    @CreationTimestamp
    @Column(name = "registered_at", nullable = false, updatable = false)
    private Instant registeredAt;

    public UserBiometrics(){
    }
}