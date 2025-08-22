package com.rununit.rununit.config;

import com.rununit.rununit.entities.Race;
import com.rununit.rununit.repositories.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Instant;

@Configuration
@Profile("test")
public class TestConfig {

    @Autowired
    private RaceRepository raceRepository;

    public void run(String... args) throws Exception {
        Race r1 = new Race(
                null,
                "Desafio 10K do RUNIT",
                "Uma corrida de 10km virtual para testar sua velocidade e resistência. Perfeita para corredores de todos os níveis.",
                Instant.now(),
                10.0,
                ZonedDateTime.parse("2025-09-15T12:00:00-03:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                ZonedDateTime.parse("2025-09-15T15:00:00-03:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                "Virtual"
        );
        raceRepository.save(r1);
    }
}