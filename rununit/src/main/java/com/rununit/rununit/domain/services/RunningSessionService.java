package com.rununit.rununit.domain.services;

import com.rununit.rununit.domain.entities.Race;
import com.rununit.rununit.domain.entities.RunningSession;
import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.infrastructure.repositories.RaceRepository;
import com.rununit.rununit.infrastructure.repositories.RunningSessionRepository;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import com.rununit.rununit.web.dto.runningsession.RunningSessionCreationRequestDto;
import com.rununit.rununit.web.dto.runningsession.RunningSessionResponseDto;
import com.rununit.rununit.web.dto.runningsession.RunningSessionUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RunningSessionService {

    private final RunningSessionRepository runningSessionRepository;
    private final UserRepository userRepository;
    private final RaceRepository raceRepository;

    public RunningSessionService(
            RunningSessionRepository runningSessionRepository,
            UserRepository userRepository,
            RaceRepository raceRepository) {
        this.runningSessionRepository = runningSessionRepository;
        this.userRepository = userRepository;
        this.raceRepository = raceRepository;
    }

    private RunningSessionResponseDto toResponseDto(RunningSession session) {
        Long raceId = (session.getRace() != null) ? session.getRace().getId() : null;

        return new RunningSessionResponseDto(
                session.getId(),
                session.getUser().getId(),
                raceId,
                session.getStartedAt(),
                session.getElapsedSeconds(),
                session.getDistanceMeters(),
                session.getPaceMinPerKm(),
                session.getAvgHeartRate(),
                session.getPeakHeartRate(),
                session.getElevationGainMeters(),
                session.getCreatedAt(),
                session.getActive()
        );
    }

    private void updateUserTotals(User user, Integer oldDistance, Integer newDistance, Long oldTime, Long newTime) {

        BigDecimal oldDistanceMeters = (oldDistance != null) ? new BigDecimal(oldDistance) : BigDecimal.ZERO;
        BigDecimal newDistanceMeters = (newDistance != null) ? new BigDecimal(newDistance) : BigDecimal.ZERO;
        BigDecimal distanceChangeMeters = newDistanceMeters.subtract(oldDistanceMeters);

        user.setTotalRunningDistance(user.getTotalRunningDistance().add(distanceChangeMeters));

        Long oldTimeSeconds = (oldTime != null) ? oldTime : 0L;
        Long newTimeSeconds = (newTime != null) ? newTime : 0L;
        Long timeChangeSeconds = newTimeSeconds - oldTimeSeconds;

        user.setTotalRunningTime(user.getTotalRunningTime() + timeChangeSeconds);

        userRepository.save(user);
    }

    @Transactional
    public RunningSessionResponseDto createSession(Long userId, RunningSessionCreationRequestDto requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Race race = null;
        if (requestDto.raceId() != null) {
            race = raceRepository.findById(requestDto.raceId())
                    .orElseThrow(() -> new RuntimeException("Race not found with ID: " + requestDto.raceId()));
        }

        RunningSession newSession = RunningSession.builder()
                .user(user)
                .race(race)
                .startedAt(requestDto.startedAt())
                .elapsedSeconds(requestDto.elapsedSeconds())
                .distanceMeters(requestDto.distanceMeters())
                .paceMinPerKm(requestDto.paceMinPerKm())
                .avgHeartRate(requestDto.avgHeartRate())
                .peakHeartRate(requestDto.peakHeartRate())
                .elevationGainMeters(requestDto.elevationGainMeters())
                .build();

        RunningSession savedSession = runningSessionRepository.save(newSession);

        updateUserTotals(
                user,
                0,
                savedSession.getDistanceMeters(),
                0L,
                savedSession.getElapsedSeconds().longValue() // Tempo novo
        );

        return toResponseDto(savedSession);
    }

    public List<RunningSessionResponseDto> getAllSessionsByUserId(Long userId) {
        return runningSessionRepository.findByUserId(userId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public RunningSessionResponseDto getSessionById(Long sessionId) {
        return runningSessionRepository.findById(sessionId)
                .map(this::toResponseDto)
                .orElseThrow(() -> new RuntimeException("RunningSession not found with ID: " + sessionId));
    }

    @Transactional
    public RunningSessionResponseDto updateSession(Long sessionId, RunningSessionUpdateRequestDto requestDto) {
        RunningSession session = runningSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("RunningSession not found with ID: " + sessionId));

        User user = session.getUser();

        Integer oldDistance = session.getDistanceMeters();
        Long oldTime = session.getElapsedSeconds().longValue();

        if (requestDto.startedAt() != null) session.setStartedAt(requestDto.startedAt());
        if (requestDto.elapsedSeconds() != null) session.setElapsedSeconds(requestDto.elapsedSeconds());
        if (requestDto.distanceMeters() != null) session.setDistanceMeters(requestDto.distanceMeters());
        if (requestDto.paceMinPerKm() != null) session.setPaceMinPerKm(requestDto.paceMinPerKm());
        if (requestDto.avgHeartRate() != null) session.setAvgHeartRate(requestDto.avgHeartRate());
        if (requestDto.peakHeartRate() != null) session.setPeakHeartRate(requestDto.peakHeartRate());
        if (requestDto.elevationGainMeters() != null) session.setElevationGainMeters(requestDto.elevationGainMeters());
        if (requestDto.active() != null) session.setActive(requestDto.active());

        if (requestDto.raceId() != null) {
            Race newRace = raceRepository.findById(requestDto.raceId())
                    .orElseThrow(() -> new RuntimeException("Race not found with ID: " + requestDto.raceId()));
            session.setRace(newRace);
        }

        RunningSession updatedSession = runningSessionRepository.save(session);

        updateUserTotals(
                user,
                oldDistance,
                updatedSession.getDistanceMeters(),
                oldTime,
                updatedSession.getElapsedSeconds().longValue()
        );

        return toResponseDto(updatedSession);
    }

    @Transactional
    public void deleteSession(Long sessionId, Long userId) {
        RunningSession session = runningSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("RunningSession not found with ID: " + sessionId));

        if (!session.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access Denied: Session does not belong to user.");
        }

        User user = session.getUser();

        updateUserTotals(
                user,
                session.getDistanceMeters(),
                0,
                session.getElapsedSeconds().longValue(),
                0L
        );
        runningSessionRepository.delete(session);
    }
}