package com.rununit.rununit.web.dto.race;

import com.rununit.rununit.domain.enums.Status; // Usamos Status, conforme a entidade
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

public record RaceCreationRequestDto(
        @NotBlank(message = "O nome da corrida é obrigatório.")
        @Size(max = 255, message = "O nome não pode exceder 255 caracteres.")
        String name,

        @NotNull(message = "A data da corrida é obrigatória.")
        @FutureOrPresent(message = "A data da corrida deve ser no presente ou futuro.")
        Instant raceDate,

        @Size(max = 150, message = "O nome do local não pode exceder 150 caracteres.")
        String venueName,

        @Size(max = 500, message = "A URL de registro não pode exceder 500 caracteres.")
        String registrationUrl,

        @Size(max = 255, message = "O contato do organizador não pode exceder 255 caracteres.")
        String organizerContact,

        @NotBlank(message = "A cidade é obrigatória.")
        @Size(max = 100, message = "A cidade não pode exceder 100 caracteres.")
        String city,

        @NotBlank(message = "O estado é obrigatório.")
        @Size(max = 100, message = "O estado não pode exceder 100 caracteres.")
        String state,

        @NotNull(message = "A latitude é obrigatória.")
        @DecimalMin(value = "-90.0", message = "Latitude inválida.")
        @DecimalMax(value = "90.0", message = "Latitude inválida.")
        BigDecimal latitude,

        @NotNull(message = "A longitude é obrigatória.")
        @DecimalMin(value = "-180.0", message = "Longitude inválida.")
        @DecimalMax(value = "180.0", message = "Longitude inválida.")
        BigDecimal longitude,

        Integer maxParticipants,

        @NotNull(message = "A distância da corrida é obrigatória.")
        @DecimalMin(value = "0.01", message = "A distância deve ser maior que zero.")
        BigDecimal raceDistanceKm,

        // O Status inicial pode ser default no Service, mas incluído se a API permitir
        @NotNull(message = "O status inicial é obrigatório.")
        Status status
) {}