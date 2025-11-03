package com.rununit.rununit.web.controllers;

import com.rununit.rununit.domain.services.UserService;
import com.rununit.rununit.web.dto.user.UserCreationRequestDto;
import com.rununit.rununit.web.dto.user.UserResponseDto;
import com.rununit.rununit.web.dto.user.UserUpdateRequestDto;
import com.rununit.rununit.web.dto.user.PasswordUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Usuário não autenticado no contexto de segurança.");
        }

        try {
            return (Long) authentication.getPrincipal();
        } catch (ClassCastException e) {
            System.err.println("ERRO: O principal não é do tipo Long. Retornando 1L como fallback inseguro.");
            return 1L;
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreationRequestDto requestDto) {
        UserResponseDto responseDto = userService.createUser(requestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.id())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR', 'USER')")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR') or #id == principal.id")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return userService.getUserResponseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> updateProfile(
            @Valid @RequestBody UserUpdateRequestDto requestDto) {

        Long userId = getAuthenticatedUserId();

        UserResponseDto responseDto = userService.updateProfile(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> updatePassword(
            @Valid @RequestBody PasswordUpdateRequestDto requestDto) {

        Long userId = getAuthenticatedUserId();
        userService.updatePassword(userId, requestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
