package com.rununit.rununit.domain.services;

import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.domain.entities.UserBiometrics;
import com.rununit.rununit.infrastructure.repositories.UserBiometricsRepository;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import com.rununit.rununit.web.dto.userbiometrics.UserBiometricsCreationRequestDto;
import com.rununit.rununit.web.dto.userbiometrics.UserBiometricsResponseDto;
import com.rununit.rununit.web.dto.userbiometrics.UserBiometricsUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Optional;

@Service
public class UserBiometricsService {

    private final UserBiometricsRepository biometricsRepository;
    private final UserRepository userRepository;

    public UserBiometricsService(UserBiometricsRepository biometricsRepository, UserRepository userRepository) {
        this.biometricsRepository = biometricsRepository;
        this.userRepository = userRepository;
    }

    private UserBiometricsResponseDto toResponseDto(UserBiometrics biometrics) {
        return new UserBiometricsResponseDto(
                biometrics.getId(),
                biometrics.getBiometricActive(),
                biometrics.getRegisteredAt()
        );
    }

    private byte[] decodeBase64(String base64String) {
        try {
            return Base64.getDecoder().decode(base64String);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Base64 format for facial embedding.");
        }
    }

    @Transactional
    public UserBiometricsResponseDto registerBiometrics(Long userId, UserBiometricsCreationRequestDto requestDto) {
        if (biometricsRepository.existsById(userId)) {
            throw new RuntimeException("Biometrics already registered for User ID: " + userId);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        byte[] embedding = decodeBase64(requestDto.facialEmbeddingBase64());

        UserBiometrics biometrics = new UserBiometrics();
        biometrics.setId(userId);
        biometrics.setUser(user);
        biometrics.setFacialEmbedding(embedding);
        biometrics.setBiometricActive(true);

        UserBiometrics savedBiometrics = biometricsRepository.save(biometrics);

        user.setHasBiometrics(true);
        userRepository.save(user);

        return toResponseDto(savedBiometrics);
    }

    public Optional<UserBiometricsResponseDto> getBiometricsStatus(Long userId) {
        return biometricsRepository.findById(userId)
                .map(this::toResponseDto);
    }

    // NOTE: A method to retrieve the actual byte[] for verification would be here, but is omitted for controller simplicity.

    @Transactional
    public UserBiometricsResponseDto updateBiometrics(Long userId, UserBiometricsUpdateRequestDto requestDto) {
        UserBiometrics biometrics = biometricsRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Biometrics not registered for User ID: " + userId));

        if (requestDto.facialEmbeddingBase64() != null) {
            byte[] embedding = decodeBase64(requestDto.facialEmbeddingBase64());
            biometrics.setFacialEmbedding(embedding);
        }

        if (requestDto.biometricActive() != null) {
            biometrics.setBiometricActive(requestDto.biometricActive());
        }

        UserBiometrics updatedBiometrics = biometricsRepository.save(biometrics);
        return toResponseDto(updatedBiometrics);
    }

    @Transactional
    public void deleteBiometrics(Long userId) {
        UserBiometrics biometrics = biometricsRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Biometrics not registered for User ID: " + userId));

        biometricsRepository.delete(biometrics);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setHasBiometrics(false);
        userRepository.save(user);
    }
}