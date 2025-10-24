package com.rununit.rununit.entities;

import com.rununit.rununit.entities.enums.Gender;
import com.rununit.rununit.entities.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class User {

    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "has_biometrics", nullable = false)
    private Boolean hasBiometrics = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserBiometrics biometrics;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, length = 10)
    private UserRole userRole;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name= "last_name", nullable = false, length = 150)
    private String lastName;

    @Setter(AccessLevel.NONE)
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name= "total_running_distance", precision = 10, scale = 2)
    private BigDecimal totalRunningDistance = BigDecimal.ZERO;

    @Column(name = "total_running_time")
    private Integer totalRunningTime = 0;

    @Column( nullable = false)
    private Boolean active = true;

    @Column(name = "profile_picture_url", length = 500)
    private String profilePictureUrl;

    @Column(length = 50)
    private String timezone;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(length = 10)
    private String locale;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_type_id", nullable = false)
    private MembershipType membershipType;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Login login;


    public User() {}

    public User(String name, String lastName, String email, String password, LocalDate birthDate, Gender gender, MembershipType membershipType, String timezone, String locale) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.gender = gender;
        this.membershipType = membershipType;
        this.timezone = timezone;
        this.locale = locale;
    }
}
