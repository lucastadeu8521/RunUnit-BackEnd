package com.rununit.rununit.domain.entities;

import com.rununit.rununit.domain.enums.Gender;
import com.rununit.rununit.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class User {

    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Login login;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserBiometrics biometrics;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RunningSession> runningSessions;

    @Column(name = "has_biometrics", nullable = false)
    private Boolean hasBiometrics = false;

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
    private Long totalRunningTime = 0L;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_type_id", nullable = false)
    private MembershipType membershipType;

    public User(String name, String lastName, LocalDate birthDate, Gender gender, String timezone, String locale, MembershipType membershipType) {
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.timezone = timezone;
        this.locale = locale;
        this.membershipType = membershipType;
    }

    public String getPassword() {
        return (this.login != null) ? this.login.getPasswordHash() : null;
    }

    public void setPassword(String password) {
        if (this.login == null) {
            this.login = new Login();
            this.login.setUser(this);
        }
        this.login.setPasswordHash(password);
    }
}