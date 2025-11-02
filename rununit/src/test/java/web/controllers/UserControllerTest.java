package web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rununit.rununit.domain.enums.Gender;
import com.rununit.rununit.domain.services.UserService;
import com.rununit.rununit.web.controllers.UserController;
import com.rununit.rununit.web.dto.user.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    private UserResponseDto createMockResponseDto(Long id, String name, String email) {
        return new UserResponseDto(
                id,
                name,
                "Silva",
                email,
                com.rununit.rununit.domain.enums.UserRole.USER,
                "url_foto.jpg",
                LocalDate.of(2000, 1, 1),
                Gender.M, // Assumindo que você tem um Enum Gender e ele se serializa
                "America/Sao_Paulo",
                "pt-BR",
                BigDecimal.ZERO,
                0L,
                true,
                Instant.now()
        );
    }

    @Test
    void shouldReturnUserWhenGetById() throws Exception {
        // 1. Arrange
        Long userId = 1L;
        String email = "joao@runit.com";
        UserResponseDto mockDto = createMockResponseDto(userId, "Joao", email);

        when(userService.getUserResponseById(userId)).thenReturn(Optional.of(mockDto));

        mockMvc.perform(get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(userId.intValue()))
                .andExpect(jsonPath("$.name").value("Joao"))
                .andExpect(jsonPath("$.email").value(email));

        verify(userService, times(1)).getUserResponseById(userId);
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        // 1. Arrange
        Long userId = 99L;

        // MOCK: Service retorna Optional.empty()
        when(userService.getUserResponseById(userId)).thenReturn(Optional.empty());

        // 2. Act & 3. Assert
        mockMvc.perform(get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Espera 404 Not Found

        // 4. Verify
        verify(userService, times(1)).getUserResponseById(userId);
    }
}
