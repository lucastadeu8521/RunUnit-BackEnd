package com.rununit.rununit.entities;

import com.rununit.rununit.entities.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "races")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Race implements Serializable {

    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(name = "race_date", nullable = false  )
    private LocalDateTime raceDate;

    @Column(name = "venue_name", length = 150)
    private String venueName;

    @Column(name = "registration_url", length = 500)
    private String registrationUrl;

    @Column(name = "organizer_contact", length = 255)
    private String organizerContact;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "race_distance_km", precision = 5, scale = 2)
    private BigDecimal raceDistanceKm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

    public Race() {
    }

    public Race(Long id, String name, LocalDateTime raceDate, String venueName, String registrationUrl, String organizerContact, String city, String state, BigDecimal latitude, BigDecimal longitude, Integer maxParticipants, BigDecimal raceDistanceKm, Status status, Instant createdAt) {
        this.name = name;
        this.raceDate = raceDate;
        this.venueName = venueName;
        this.registrationUrl = registrationUrl;
        this.organizerContact = organizerContact;
        this.city = city;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.maxParticipants = maxParticipants;
        this.raceDistanceKm = raceDistanceKm;
        this.status = status;
        this.createdAt = createdAt;
    }
}


