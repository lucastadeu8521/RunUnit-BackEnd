package web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rununit.rununit.domain.entities.Login;
import com.rununit.rununit.domain.entities.MembershipType;
import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.domain.enums.Gender;
import com.rununit.rununit.domain.enums.UserRole;
import com.rununit.rununit.domain.services.AuthService;
import com.rununit.rununit.infrastructure.repositories.MembershipTypeRepository;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import com.rununit.rununit.infrastructure.security.JwtTokenUtil;
import com.rununit.rununit.web.controllers.AuthController;
import com.rununit.rununit.web.dto.user.UserCreationRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

record AuthRequest(String email, String password) {}

@WebMvcTest(AuthController.class)
@Import(AuthControllerTest.TestConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MembershipTypeRepository membershipTypeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private User createMockUser(String email, Long id) {

        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(id);

        when(user.getName()).thenReturn("Teste");
        when(user.getLastName()).thenReturn("Runit");
        when(user.getUserRole()).thenReturn(UserRole.USER);

        Login login = new Login();
        login.setEmail(email);
        when(user.getLogin()).thenReturn(login);

        return user;
    }


    @Test
    void shouldLoginSuccessfullyAndReturnJwtToken() throws Exception {
        AuthRequest loginRequest = new AuthRequest("corredor@runit.com", "senha123");
        String expectedToken = "token.jwt.valido";
        User mockUser = createMockUser(loginRequest.email(), 1L);

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(Mockito.mock(Authentication.class));
        when(userRepository.findByLoginEmail(loginRequest.email())).thenReturn(Optional.of(mockUser));
        when(jwtTokenUtil.generateToken(mockUser)).thenReturn(expectedToken);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedToken))
                .andExpect(jsonPath("$.email").value(loginRequest.email()));

        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    // O método @Test estava incompleto, removi a linha solta.

    @Test
    void shouldReturn400BadRequestForInvalidRegistrationData() throws Exception {

        UserCreationRequestDto invalidRequest = new UserCreationRequestDto(
                null,
                "Invalid",
                LocalDate.of(1995, 1, 1),
                Gender.M,
                "America/Sao_Paulo",
                "pt-BR", // locale
                "email_invalido.com",
                "short"
        );

        // 2. Act & 3. Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.errors").exists());

        verify(membershipTypeRepository, never()).findById(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldReturn401ForInvalidCredentials() throws Exception {
        AuthRequest loginRequest = new AuthRequest("usuario@invalido.com", "senhaerrada");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());

        verify(userRepository, never()).findByLoginEmail(any());
    }

    @Test
    void shouldRegisterNewUserAndReturnDto() throws Exception {
        UserCreationRequestDto registerRequest = new UserCreationRequestDto(
                "Novo",
                "User",
                LocalDate.of(1995, 1, 1),
                Gender.M,
                "America/Sao_Paulo",
                "pt-BR",
                "novo@runit.com",
                "senhaforte"
        );

        MembershipType defaultMembership = Mockito.mock(MembershipType.class);
        User savedUser = createMockUser(registerRequest.email(), 2L);
        when(savedUser.getName()).thenReturn(registerRequest.name());
        when(savedUser.getLastName()).thenReturn(registerRequest.lastName());

        when(membershipTypeRepository.findById(1L)).thenReturn(Optional.of(defaultMembership));
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("novo@runit.com"));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Configuration
    static class TestConfig {

        @Bean
        public AuthenticationManager authenticationManager() {
            return Mockito.mock(AuthenticationManager.class);
        }

        @Bean
        public JwtTokenUtil jwtTokenUtil() {
            return Mockito.mock(JwtTokenUtil.class);
        }

        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }

        @Bean
        public MembershipTypeRepository membershipTypeRepository() {
            return Mockito.mock(MembershipTypeRepository.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return Mockito.mock(PasswordEncoder.class);
        }

        @Bean
        public AuthService authService() {
            return Mockito.mock(AuthService.class);
        }
    }
}
