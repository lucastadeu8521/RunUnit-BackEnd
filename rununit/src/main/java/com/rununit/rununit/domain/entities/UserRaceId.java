package com.rununit.rununit.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRaceId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "race_id")
    private Long raceId;
}