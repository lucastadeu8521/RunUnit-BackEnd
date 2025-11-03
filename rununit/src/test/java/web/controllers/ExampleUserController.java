package web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Este Controller é um exemplo que demonstra a aplicação de regras de autorização
 * baseadas em roles (ADMIN, EDITOR, USER) usando a anotação @PreAuthorize.
 * * NOTA: Para que as regras de segurança em nível de método funcionem, o
 * SecurityConfig deve ter @EnableMethodSecurity.
 */
@RestController
@RequestMapping("/api/example")
public class ExampleUserController {

    /**
     * Endpoint para registrar uma corrida.
     * Acessível por qualquer usuário logado (USER, EDITOR, ADMIN).
     * Regra: isAuthenticated() ou hasAnyRole('USER', 'EDITOR', 'ADMIN')
     */
    @PostMapping("/runs")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> postNewRun() {
        return ResponseEntity.ok("Corrida registrada: Acesso permitido para qualquer usuário autenticado.");
    }

    /**
     * Endpoint para buscar dados de perfil sensíveis de outro usuário.
     * Acessível apenas por EDITOR ou ADMIN.
     * Regra: hasAnyRole('ADMIN', 'EDITOR')
     */
    @GetMapping("/users/{id}/data")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<String> getUserData(@PathVariable Long id) {
        return ResponseEntity.ok("Dados do usuário " + id + " visualizados: Acesso permitido para ADMIN ou EDITOR.");
    }

    /**
     * Endpoint para atualizar o perfil de qualquer usuário.
     * Acessível apenas por EDITOR ou ADMIN.
     * Regra: hasAnyRole('ADMIN', 'EDITOR')
     */
    @PutMapping("/users/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<String> updateUser(@PathVariable Long id) {
        return ResponseEntity.ok("Usuário " + id + " atualizado: Acesso permitido para ADMIN ou EDITOR.");
    }


    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok("Usuário " + id + " deletado: Acesso permitido APENAS para ADMIN.");
    }
}
