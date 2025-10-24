package com.rununit.rununit.services;

import com.rununit.rununit.entities.User;
import com.rununit.rununit.repositories.UserRepository;
import com.rununit.rununit.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final String GOOGLE_CLIENT_ID = "SEU_CLIENT_ID";
    private final String GOOGLE_CLIENT_SECRET = "SEU_CLIENT_SECRET";
    private final String GOOGLE_REDIRECT_URI = "http://localhost:8080/api/auth/google/callback";

    private final RestTemplate restTemplate = new RestTemplate();


    public String authenticateWithGoogle(String code) {
        // 1. Trocar o "code" por um access_token do Google
        String tokenUrl = "https://oauth2.googleapis.com/token" +
                "?code=" + code +
                "&client_id=" + GOOGLE_CLIENT_ID +
                "&client_secret=" + GOOGLE_CLIENT_SECRET +
                "&redirect_uri=" + GOOGLE_REDIRECT_URI +
                "&grant_type=authorization_code";

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
            newUser.setPassword(passwordEncoder.encode("google_oauth2_dummy_password"));
            return userRepository.save(newUser);
        });

        return jwtTokenUtil.generateToken(user);
    }


    public String getGoogleAuthUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=" + GOOGLE_CLIENT_ID +
                "&redirect_uri=" + GOOGLE_REDIRECT_URI +
                "&response_type=code" +
                "&scope=openid%20profile%20email";
    }
}