package web.security;

import com.rununit.rununit.infrastructure.security.JwtAuthenticationEntryPoint;
import com.rununit.rununit.infrastructure.security.JwtAuthenticationFilter;
import com.rununit.rununit.infrastructure.security.JwtTokenUtil;
import com.rununit.rununit.config.SecurityConfig;

import com.rununit.rununit.web.controllers.UserController;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import web.controllers.ExampleUserController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Teste de integração focado em verificar as regras de autorização (@PreAuthorize)
 * definidas no ExampleUserController para as diferentes roles (USER, EDITOR, ADMIN).
 * * NOTA: Este teste precisa que o SecurityConfig e o CustomUserDetailsService estejam
 * funcionando corretamente para injetar o usuário mockado com a role correta.
 */
@WebMvcTest(controllers = ExampleUserController.class)
@Import(SecurityConfig.class)
public class AuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "USER")
    void asUser_shouldAccessRuns_butBeForbiddenToUpdateOrDeleteUsers() throws Exception {

        mockMvc.perform(post("/api/example/runs"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/example/users/1"))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/example/users/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void asEditor_shouldAccessRunsAndUpdateUsers_butBeForbiddenToDelete() throws Exception {
        mockMvc.perform(post("/api/example/runs"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/example/users/1"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/example/users/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void asAdmin_shouldAccessAllEndpoints() throws Exception {
        mockMvc.perform(post("/api/example/runs"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/example/users/1"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/example/users/1"))
                .andExpect(status().isOk());
    }


    @Test
    void asUnauthenticatedUser_shouldBeDeniedAccess() throws Exception {

        mockMvc.perform(post("/api/example/runs"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put("/api/example/users/1"))
                .andExpect(status().isUnauthorized());
    }
}
