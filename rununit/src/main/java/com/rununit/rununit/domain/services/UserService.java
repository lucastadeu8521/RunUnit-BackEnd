package com.rununit.rununit.domain.services;

import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.domain.entities.Login;
import com.rununit.rununit.domain.entities.MembershipType;
import com.rununit.rununit.domain.enums.UserRole;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import com.rununit.rununit.infrastructure.repositories.LoginRepository;
import com.rununit.rununit.infrastructure.repositories.MembershipTypeRepository;
import com.rununit.rununit.web.dto.user.UserCreationRequestDto;
import com.rununit.rununit.web.dto.user.UserResponseDto;
import com.rununit.rununit.web.dto.user.UserUpdateRequestDto;
import com.rununit.rununit.web.dto.user.PasswordUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
    private final MembershipTypeRepository membershipTypeRepository;

    public UserService(UserRepository userRepository, LoginRepository loginRepository, MembershipTypeRepository membershipTypeRepository) {
        this.userRepository = userRepository;
        this.loginRepository = loginRepository;
        this.membershipTypeRepository = membershipTypeRepository;
    }

    private UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getLogin().getEmail(),
                user.getUserRole(),
                user.getProfilePictureUrl(),
                user.getBirthDate(),
                user.getGender(),
                user.getTimezone(),
                user.getLocale(),
                user.getTotalRunningDistance(),
                user.getTotalRunningTime(),
                user.getActive(),
                user.getCreatedAt()
        );
    }

    @Transactional
    public UserResponseDto createUser(UserCreationRequestDto requestDto) {

        if (loginRepository.existsByEmail(requestDto.email())) {
            throw new RuntimeException("Email already registered");
        }

        MembershipType defaultMembership = membershipTypeRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default Membership Type not found"));

        User newUser = User.builder()
                .name(requestDto.name())
                .lastName(requestDto.lastName())
                .birthDate(requestDto.birthDate())
                .gender(requestDto.gender())
                .timezone(requestDto.timezone())
                .locale(requestDto.locale())
                .userRole(UserRole.USER)
                .membershipType(defaultMembership)
                .totalRunningDistance(BigDecimal.ZERO)
                .totalRunningTime(0L)
                .active(true)
                .hasBiometrics(false)
                .build();

        User savedUser = userRepository.save(newUser);

        Login newLogin = new Login();
        newLogin.setUserId(savedUser.getId());
        newLogin.setUser(savedUser);
        newLogin.setEmail(requestDto.email());

        newLogin.setPasswordHash(requestDto.password());

        loginRepository.save(newLogin);

        savedUser.setLogin(newLogin);
        userRepository.save(savedUser);

        return toResponseDto(savedUser);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDto> getUserResponseById(Long id) {
        return userRepository.findById(id).map(this::toResponseDto);
    }

    @Transactional
    public UserResponseDto updateProfile(Long userId, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (requestDto.name() != null) user.setName(requestDto.name());
        if (requestDto.lastName() != null) user.setLastName(requestDto.lastName());
        if (requestDto.birthDate() != null) user.setBirthDate(requestDto.birthDate());
        if (requestDto.gender() != null) user.setGender(requestDto.gender());
        if (requestDto.timezone() != null) user.setTimezone(requestDto.timezone());
        if (requestDto.locale() != null) user.setLocale(requestDto.locale());
        if (requestDto.profilePictureUrl() != null) user.setProfilePictureUrl(requestDto.profilePictureUrl());

        User updatedUser = userRepository.save(user);
        return toResponseDto(updatedUser);
    }

    @Transactional
    public void updatePassword(Long userId, PasswordUpdateRequestDto requestDto) {
        if (!requestDto.newPassword().equals(requestDto.newPasswordConfirmation())) {
            throw new RuntimeException("New password and confirmation do not match.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Login login = user.getLogin();

        if (!requestDto.currentPassword().equals(login.getPasswordHash())) {
            throw new RuntimeException("Invalid current password.");
        }

        login.setPasswordHash(requestDto.newPassword());

        loginRepository.save(login);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        userRepository.delete(user);
    }
}