package com.rununit.rununit.domain.services;

import com.rununit.rununit.domain.entities.Race;
import com.rununit.rununit.infrastructure.repositories.RaceRepository;
import com.rununit.rununit.web.dto.race.RaceCreationRequestDto;
import com.rununit.rununit.web.dto.race.RaceResponseDto;
import com.rununit.rununit.web.dto.race.RaceUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RaceService {

    private final RaceRepository raceRepository;

    public RaceService(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    private RaceResponseDto toResponseDto(Race race) {
        return new RaceResponseDto(
                race.getId(),
                race.getName(),
                race.getRaceDate(),
                race.getVenueName(),
                race.getRegistrationUrl(),
                race.getOrganizerContact(),
                race.getCity(),
                race.getState(),
                race.getLatitude(),
                race.getLongitude(),
                race.getMaxParticipants(),
                race.getRaceDistanceKm(),
                race.getStatus(),
                race.getCreatedAt()
        );
    }

    private Race toEntity(RaceCreationRequestDto requestDto) {
        Race race = new Race();
        race.setName(requestDto.name());
        race.setRaceDate(requestDto.raceDate());
        race.setVenueName(requestDto.venueName());
        race.setRegistrationUrl(requestDto.registrationUrl());
        race.setOrganizerContact(requestDto.organizerContact());
        race.setCity(requestDto.city());
        race.setState(requestDto.state());
        race.setLatitude(requestDto.latitude());
        race.setLongitude(requestDto.longitude());
        race.setMaxParticipants(requestDto.maxParticipants());
        race.setRaceDistanceKm(requestDto.raceDistanceKm());
        race.setStatus(requestDto.status());
        return race;
    }

    @Transactional
    public RaceResponseDto createRace(RaceCreationRequestDto requestDto) {
        Race newRace = toEntity(requestDto);
        Race savedRace = raceRepository.save(newRace);
        return toResponseDto(savedRace);
    }

    public List<RaceResponseDto> findAllRaces() {
        return raceRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public RaceResponseDto findRaceById(Long id) {
        return raceRepository.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Race not found with ID: " + id));
    }

    @Transactional
    public RaceResponseDto updateRace(Long id, RaceUpdateRequestDto requestDto) {
        Race race = raceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Race not found with ID: " + id));

        if (requestDto.name() != null) race.setName(requestDto.name());
        if (requestDto.raceDate() != null) race.setRaceDate(requestDto.raceDate());
        if (requestDto.venueName() != null) race.setVenueName(requestDto.venueName());
        if (requestDto.registrationUrl() != null) race.setRegistrationUrl(requestDto.registrationUrl());
        if (requestDto.organizerContact() != null) race.setOrganizerContact(requestDto.organizerContact());
        if (requestDto.city() != null) race.setCity(requestDto.city());
        if (requestDto.state() != null) race.setState(requestDto.state());
        if (requestDto.latitude() != null) race.setLatitude(requestDto.latitude());
        if (requestDto.longitude() != null) race.setLongitude(requestDto.longitude());
        if (requestDto.maxParticipants() != null) race.setMaxParticipants(requestDto.maxParticipants());
        if (requestDto.raceDistanceKm() != null) race.setRaceDistanceKm(requestDto.raceDistanceKm());
        if (requestDto.status() != null) race.setStatus(requestDto.status());

        Race updatedRace = raceRepository.save(race);
        return toResponseDto(updatedRace);
    }

    @Transactional
    public void deleteRace(Long id) {
        if (!raceRepository.existsById(id)) {
            throw new RuntimeException("Race not found with ID: " + id);
        }
        raceRepository.deleteById(id);
    }

    public List<RaceResponseDto> searchRacesByFilters(
            String name,
            String location,
            Instant startDate,
            Instant endDate
    ) {
        return raceRepository.findByFilters(name, location, startDate, endDate).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<RaceResponseDto> searchRacesByName(String name) {
        return raceRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}