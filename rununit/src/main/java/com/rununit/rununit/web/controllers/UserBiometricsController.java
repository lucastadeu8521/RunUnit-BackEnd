package com.rununit.rununit.web.controllers;

import com.rununit.rununit.domain.services.UserBiometricsService;
import com.rununit.rununit.web.dto.userbiometrics.UserBiometricsCreationRequestDto;
import com.rununit.rununit.web.dto.userbiometrics.UserBiometricsResponseDto;
import com.rununit.rununit.web.dto.userbiometrics.UserBiometricsUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/my/biometrics")
public class UserBiometricsController {

    private final UserBiometricsService biometricsService;

    public UserBiometricsController(UserBiometricsService biometricsService) {
        this.biometricsService = biometricsService;
    }

    private Long getAuthenticatedUserId() {
        return 1L;
    }

    @PostMapping
    public ResponseEntity<UserBiometricsResponseDto> registerBiometrics(
            @Valid @RequestBody UserBiometricsCreationRequestDto requestDto) {

        Long userId = getAuthenticatedUserId();
        UserBiometricsResponseDto responseDto = biometricsService.registerBiometrics(userId, requestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .build()
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<UserBiometricsResponseDto> getBiometricsStatus() {
        Long userId = getAuthenticatedUserId();
        return biometricsService.getBiometricsStatus(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<UserBiometricsResponseDto> updateBiometrics(
            @Valid @RequestBody UserBiometricsUpdateRequestDto requestDto) {

        Long userId = getAuthenticatedUserId();
        UserBiometricsResponseDto responseDto = biometricsService.updateBiometrics(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBiometrics() {
        Long userId = getAuthenticatedUserId();
        biometricsService.deleteBiometrics(userId);
        return ResponseEntity.noContent().build();
    }
}