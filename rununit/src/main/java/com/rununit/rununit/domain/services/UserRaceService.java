package com.rununit.rununit.domain.services;

import com.rununit.rununit.domain.entities.Race;
import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.domain.entities.UserRace;
import com.rununit.rununit.domain.entities.UserRaceId;
import com.rununit.rununit.infrastructure.repositories.RaceRepository;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import com.rununit.rununit.infrastructure.repositories.UserRaceRepository;
import com.rununit.rununit.web.dto.userrace.UserRaceCreationRequestDto;
import com.rununit.rununit.web.dto.userrace.UserRaceResponseDto;
import com.rununit.rununit.web.dto.userrace.UserRaceUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRaceService {

    private final UserRaceRepository userRaceRepository;
    private final UserRepository userRepository;
    private final RaceRepository raceRepository;

    public UserRaceService(UserRaceRepository userRaceRepository, UserRepository userRepository, RaceRepository raceRepository) {
        this.userRaceRepository = userRaceRepository;
        this.userRepository = userRepository;
        this.raceRepository = raceRepository;
    }

    private UserRaceResponseDto toResponseDto(UserRace userRace) {
        return new UserRaceResponseDto(
                userRace.getUser().getId(),
                userRace.getRace().getId(),
                userRace.getTag(),
                userRace.getCreatedAt(),
                userRace.getActive()
        );
    }

    private UserRace findUserRaceByIds(Long userId, Long raceId) {
        return userRaceRepository.findByIdUserIdAndIdRaceId(userId, raceId)
                .orElseThrow(() -> new RuntimeException("UserRace registration not found."));
    }

    @Transactional
    public UserRaceResponseDto registerUserForRace(Long userId, UserRaceCreationRequestDto requestDto) {
        Long raceId = requestDto.raceId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Race race = raceRepository.findById(raceId)
                .orElseThrow(() -> new RuntimeException("Race not found with ID: " + raceId));

        if (userRaceRepository.findByIdUserIdAndIdRaceId(userId, raceId).isPresent()) {
            throw new RuntimeException("User is already registered for this race.");
        }

        UserRaceId id = new UserRaceId(userId, raceId);

        UserRace userRace = UserRace.builder()
                .id(id)
                .user(user)
                .race(race)
                .tag(requestDto.tag())
                .active(true)
                .build();

        UserRace savedUserRace = userRaceRepository.save(userRace);
        return toResponseDto(savedUserRace);
    }

    public List<UserRaceResponseDto> getUserRaces(Long userId) {
        return userRaceRepository.findByIdUserId(userId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<UserRaceResponseDto> getUsersInRace(Long raceId) {
        return userRaceRepository.findByIdRaceId(raceId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public UserRaceResponseDto getUserRaceDetails(Long userId, Long raceId) {
        return toResponseDto(findUserRaceByIds(userId, raceId));
    }

    @Transactional
    public UserRaceResponseDto updateRegistration(Long userId, Long raceId, UserRaceUpdateRequestDto requestDto) {
        UserRace userRace = findUserRaceByIds(userId, raceId);

        if (requestDto.tag() != null) {
            userRace.setTag(requestDto.tag());
        }
        if (requestDto.active() != null) {
            userRace.setActive(requestDto.active());
        }

        UserRace updatedUserRace = userRaceRepository.save(userRace);
        return toResponseDto(updatedUserRace);
    }

    @Transactional
    public void unregisterUserFromRace(Long userId, Long raceId) {
        UserRace userRace = findUserRaceByIds(userId, raceId);

        userRaceRepository.delete(userRace);
    }
}