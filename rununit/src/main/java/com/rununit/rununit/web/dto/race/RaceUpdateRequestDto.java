package com.rununit.rununit.web.dto.race;

import com.rununit.rununit.domain.enums.Status;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

public record RaceUpdateRequestDto(
        @Size(max = 255, message = "O nome não pode exceder 255 caracteres.")
        String name,

        @FutureOrPresent(message = "A data da corrida deve ser no presente ou futuro.")
        Instant raceDate,

        @Size(max = 150, message = "O nome do local não pode exceder 150 caracteres.")
        String venueName,

        @Size(max = 500, message = "A URL de registro não pode exceder 500 caracteres.")
        String registrationUrl,

        @Size(max = 255, message = "O contato do organizador não pode exceder 255 caracteres.")
        String organizerContact,

        @Size(max = 100, message = "A cidade não pode exceder 100 caracteres.")
        String city,

        @Size(max = 100, message = "O estado não pode exceder 100 caracteres.")
        String state,

        @DecimalMin(value = "-90.0", message = "Latitude inválida.")
        @DecimalMax(value = "90.0", message = "Latitude inválida.")
        BigDecimal latitude,

        @DecimalMin(value = "-180.0", message = "Longitude inválida.")
        @DecimalMax(value = "180.0", message = "Longitude inválida.")
        BigDecimal longitude,

        Integer maxParticipants,

        @DecimalMin(value = "0.01", message = "A distância deve ser maior que zero.")
        BigDecimal raceDistanceKm,

        Status status
) {}