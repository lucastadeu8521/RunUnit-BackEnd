package com.rununit.rununit.domain.services;

import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.domain.entities.Login;
import com.rununit.rununit.domain.entities.MembershipType;
import com.rununit.rununit.domain.enums.UserRole;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import com.rununit.rununit.infrastructure.repositories.LoginRepository;
import com.rununit.rununit.infrastructure.repositories.MembershipTypeRepository; // Necessário para buscar o tipo de membro
import com.rununit.rununit.web.dto.user.UserCreationRequestDto;
import com.rununit.rununit.web.dto.user.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
    private final MembershipTypeRepository membershipTypeRepository;

    public UserService(UserRepository userRepository, LoginRepository loginRepository, MembershipTypeRepository membershipTypeRepository /*, PasswordEncoder passwordEncoder */) {
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
                user.getTotalRunningDistance(),
                user.getTotalRunningTime(),
                user.getActive(),
                user.getProfilePictureUrl(),
                user.getBirthDate(),
                user.getGender(),
                user.getCreatedAt()
        );
    }

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

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}