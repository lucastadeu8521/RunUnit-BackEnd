package com.rununit.rununit.web.controllers;

import com.rununit.rununit.domain.services.UserRaceService;
import com.rununit.rununit.web.dto.userrace.UserRaceCreationRequestDto;
import com.rununit.rununit.web.dto.userrace.UserRaceResponseDto;
import com.rununit.rununit.web.dto.userrace.UserRaceUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/user-races")
public class UserRaceController {

    private final UserRaceService userRaceService;

    public UserRaceController(UserRaceService userRaceService) {
        this.userRaceService = userRaceService;
    }

    private Long getAuthenticatedUserId() {
        return 1L;
    }

    @PostMapping
    public ResponseEntity<UserRaceResponseDto> registerForRace(
            @Valid @RequestBody UserRaceCreationRequestDto requestDto) {

        Long userId = getAuthenticatedUserId();
        UserRaceResponseDto responseDto = userRaceService.registerUserForRace(userId, requestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{raceId}")
                .buildAndExpand(responseDto.raceId())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping("/my")
    public List<UserRaceResponseDto> getMyRaces() {
        Long userId = getAuthenticatedUserId();
        return userRaceService.getUserRaces(userId);
    }

    @GetMapping("/race/{raceId}")
    public List<UserRaceResponseDto> getUsersInRace(@PathVariable Long raceId) {
        return userRaceService.getUsersInRace(raceId);
    }

    @GetMapping("/{raceId}")
    public ResponseEntity<UserRaceResponseDto> getMyRaceDetails(@PathVariable Long raceId) {
        Long userId = getAuthenticatedUserId();
        UserRaceResponseDto responseDto = userRaceService.getUserRaceDetails(userId, raceId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{raceId}")
    public ResponseEntity<UserRaceResponseDto> updateRegistration(
            @PathVariable Long raceId,
            @Valid @RequestBody UserRaceUpdateRequestDto requestDto) {

        Long userId = getAuthenticatedUserId();
        UserRaceResponseDto responseDto = userRaceService.updateRegistration(userId, raceId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{raceId}")
    public ResponseEntity<Void> unregisterFromRace(@PathVariable Long raceId) {
        Long userId = getAuthenticatedUserId();
        userRaceService.unregisterUserFromRace(userId, raceId);
        return ResponseEntity.noContent().build();
    }
}