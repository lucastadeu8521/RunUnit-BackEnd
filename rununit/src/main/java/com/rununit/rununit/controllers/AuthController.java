package com.rununit.rununit.controllers;

import com.rununit.rununit.entities.User;
import com.rununit.rununit.repositories.UserRepository;
import com.rununit.rununit.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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

    private final String GOOGLE_CLIENT_ID = "SEU_CLIENT_ID";
    private final String GOOGLE_CLIENT_SECRET = "SEU_CLIENT_SECRET";
    private final String GOOGLE_REDIRECT_URI = "http://localhost:8080/api/auth/google/callback";

    // ------------------ LOGIN NORMAL ------------------
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

    // ------------------ REGISTRO ------------------
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // ------------------ GOOGLE LOGIN ------------------
    @GetMapping("/google")
    public String redirectToGoogle() {
        String googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=" + GOOGLE_CLIENT_ID +
                "&redirect_uri=" + GOOGLE_REDIRECT_URI +
                "&response_type=code" +
                "&scope=openid%20profile%20email";
        return "Acesse o link para login com Google: " + googleAuthUrl;
    }

    @GetMapping("/google/callback")
    public String googleCallback(@RequestParam("code") String code) {
        // Trocar o "code" por um access_token do Google
        String tokenUrl = "https://oauth2.googleapis.com/token" +
                "?code=" + code +
                "&client_id=" + GOOGLE_CLIENT_ID +
                "&client_secret=" + GOOGLE_CLIENT_SECRET +
                "&redirect_uri=" + GOOGLE_REDIRECT_URI +
                "&grant_type=authorization_code";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> tokenResponse = restTemplate.postForObject(tokenUrl, null, Map.class);

        if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
            throw new RuntimeException("Erro ao obter token do Google");
        }

        String accessToken = (String) tokenResponse.get("access_token");


        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
        Map<String, Object> userInfo = restTemplate.getForObject(userInfoUrl, Map.class);

        if (userInfo == null || !userInfo.containsKey("email")) {
            throw new RuntimeException("Erro ao obter informações do usuário Google");
        }

        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setPassword(passwordEncoder.encode("google_oauth2")); // senha dummy
            return userRepository.save(newUser);
        });
        return jwtTokenUtil.generateToken(user);
    }
}
