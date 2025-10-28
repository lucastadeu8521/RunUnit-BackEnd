package com.rununit.rununit.domain.services;

import com.rununit.rununit.domain.entities.Login;
import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.domain.enums.Gender;
import com.rununit.rununit.domain.enums.UserRole;
import com.rununit.rununit.infrastructure.repositories.LoginRepository;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import com.rununit.rununit.infrastructure.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // Importação essencial para @Value
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException; // Adicionado para melhor tratamento de erros

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {


    @Value("${google.client.id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${google.client.secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${google.redirect.uri}")
    private String GOOGLE_REDIRECT_URI;


    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginRepository loginRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public String authenticateWithGoogle(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token" +
                "?code=" + code +
                "&client_id=" + GOOGLE_CLIENT_ID +
                "&client_secret=" + GOOGLE_CLIENT_SECRET +
                "&redirect_uri=" + GOOGLE_REDIRECT_URI +
                "&grant_type=authorization_code";

        Map<String, Object> tokenResponse;
        try {
            tokenResponse = restTemplate.postForObject(tokenUrl, null, Map.class);
        } catch (HttpClientErrorException e) {
            throw new IllegalStateException("Falha na troca de código por token do Google: " + e.getMessage());
        }

        if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
            throw new IllegalStateException("Resposta do token do Google incompleta.");
        }

        String accessToken = (String) tokenResponse.get("access_token");
        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
        Map<String, Object> userInfo = restTemplate.getForObject(userInfoUrl, Map.class);

        if (userInfo == null || !userInfo.containsKey("email")) {
            throw new IllegalStateException("Erro ao obter informações de e-mail do usuário Google.");
        }

        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        Optional<Login> existingLogin = loginRepository.findByEmail(email);

        Login login;

        if (existingLogin.isPresent()) {
            login = existingLogin.get();
        } else {


            String firstName = name;
            String lastName = "";
            if (name.contains(" ")) {
                firstName = name.substring(0, name.lastIndexOf(" "));
                lastName = name.substring(name.lastIndexOf(" ") + 1);
            }

            User newUser = User.builder()
                    .name(firstName)
                    .lastName(lastName)
                    .userRole(UserRole.USER)
                    .birthDate(LocalDate.of(2000, 1, 1))
                    .gender(Gender.F)
                    .timezone("UTC")
                    .locale("pt_BR")
                    .membershipType(null)
                    .build();

            login = Login.builder()
                    .email(email)
                    .passwordHash(passwordEncoder.encode("oauth2_dummy_password"))
                    .user(newUser)
                    .build();
            newUser.setLogin(login);
            userRepository.save(newUser);
        }

        return jwtTokenUtil.generateToken(login.getUser());
    }


    public String getGoogleAuthUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=" + GOOGLE_CLIENT_ID +
                "&redirect_uri=" + GOOGLE_REDIRECT_URI +
                "&response_type=code" +
                "&scope=openid%20profile%20email";
    }
}