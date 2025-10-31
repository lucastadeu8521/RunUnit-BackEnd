package com.rununit.rununit.web.controllers;

import com.rununit.rununit.domain.entities.Login;
import com.rununit.rununit.domain.entities.MembershipType;
import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.domain.enums.UserRole;
import com.rununit.rununit.domain.services.AuthService;
import com.rununit.rununit.infrastructure.repositories.MembershipTypeRepository;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import com.rununit.rununit.infrastructure.security.JwtTokenUtil;
import com.rununit.rununit.web.dto.user.UserCreationRequestDto;
import com.rununit.rununit.web.dto.user.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipTypeRepository membershipTypeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;
    private record AuthRequest(String email, String password) {}

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            User user = userRepository.findByLoginEmail(request.email())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            String token = jwtTokenUtil.generateToken(user);

            return ResponseEntity.ok(new LoginResponse(
                    token,
                    user.getId(),
                    user.getName(),
                    user.getLastName(),
                    user.getLogin().getEmail(),
                    user.getUserRole()
            ));

        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciais inválidas");
        }
    }
    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserCreationRequestDto request) {
        MembershipType defaultMembership = membershipTypeRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Tipo de associação padrão não encontrado"));

        User user = new User();
        user.setName(request.name());
        user.setLastName(request.lastName());
        user.setBirthDate(request.birthDate());
        user.setGender(request.gender());
        user.setTimezone(request.timezone());
        user.setLocale(request.locale());
        user.setMembershipType(defaultMembership);

        Login login = new Login();
        login.setUser(user);
        login.setEmail(request.email());
        login.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setLogin(login);
        User savedUser = userRepository.save(user);

        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getLastName(),
                savedUser.getLogin().getEmail(),
                savedUser.getUserRole(),
                savedUser.getProfilePictureUrl(),
                savedUser.getBirthDate(),
                savedUser.getGender(),
                savedUser.getTimezone(),
                savedUser.getLocale(),
                savedUser.getTotalRunningDistance(),
                savedUser.getTotalRunningTime(),
                savedUser.isActive(),
                savedUser.getCreatedAt()
        );
    }
    @GetMapping("/google")
    public ResponseEntity<String> redirectToGoogle() {
        String googleAuthUrl = authService.getGoogleAuthUrl();
        return ResponseEntity.ok("Acesse o link para login com Google: " + googleAuthUrl);
    }

    @GetMapping("/google/callback")
    public ResponseEntity<LoginResponse> googleCallback(@RequestParam("code") String code) {
        String token = authService.authenticateWithGoogle(code);
        User user = jwtTokenUtil.extractUserFromToken(token);

        return ResponseEntity.ok(new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getLogin().getEmail(),
                user.getUserRole()
        ));
    }

    public record LoginResponse(
            String token,
            Long id,
            String name,
            String lastName,
            String email,
            UserRole role
    ) {}
}
