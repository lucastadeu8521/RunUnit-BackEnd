package com.rununit.rununit.web.controllers;

import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import com.rununit.rununit.infrastructure.security.JwtTokenUtil;
import com.rununit.rununit.domain.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;



    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            return jwtTokenUtil.generateToken(user);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciais inválidas");
        }
    }


    @PostMapping("/register")
    public User register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    @GetMapping("/google")
    public ResponseEntity<String> redirectToGoogle() {
        String googleAuthUrl = authService.getGoogleAuthUrl();
        return ResponseEntity.ok("Acesse o link para login com Google: " + googleAuthUrl);
    }

    @GetMapping("/google/callback")
    public String googleCallback(@RequestParam("code") String code) {
        return authService.authenticateWithGoogle(code);
    }
}
