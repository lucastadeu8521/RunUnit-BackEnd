package com.rununit.rununit.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
    private long id;

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
    private LocalDateTime registeredAt;

    public UserBiometrics(){
    }

    public UserBiometrics(User user, byte[] facialEmbedding) {
        this.user = user;
        this.facialEmbedding = facialEmbedding;
    }

}
