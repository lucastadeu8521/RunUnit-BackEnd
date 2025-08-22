package com.rununit.rununit.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_race")
public class Race implements Serializable {

    @Id
    @UuidGenerator
    private UUID id;
    private String name;
    private String description;
    private Instant createdAt;

    private Double distance;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private String location;

    public Race() {
    }

    public Race(UUID id, String name, String description, Instant createdAt, Double distance, ZonedDateTime startTime, ZonedDateTime endTime, String location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.distance = distance;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Race race = (Race) o;
        return Objects.equals(id, race.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}


