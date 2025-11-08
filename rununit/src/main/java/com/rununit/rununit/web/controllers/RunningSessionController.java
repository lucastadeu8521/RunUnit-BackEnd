package com.rununit.rununit.web.controllers;

import com.rununit.rununit.domain.services.RunningSessionService;
import com.rununit.rununit.web.dto.runningsession.RunningSessionCreationRequestDto;
import com.rununit.rununit.web.dto.runningsession.RunningSessionResponseDto;
import com.rununit.rununit.web.dto.runningsession.RunningSessionUpdateRequestDto;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sessions")
public class RunningSessionController {

    private final RunningSessionService sessionService;
    private final UserRepository userRepository;

    public RunningSessionController(
            RunningSessionService sessionService,
            UserRepository userRepository) {
        this.sessionService = sessionService;
        this.userRepository = userRepository;
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não autenticado no contexto.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            String principalValue = ((UserDetails) principal).getUsername();


            try {
                return Long.valueOf(principalValue);
            } catch (NumberFormatException e) {
                Optional<Long> userIdOptional = userRepository.findByLoginEmail(principalValue)
                        .map(user -> user.getId());

                return userIdOptional.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Não foi possível encontrar o ID do usuário autenticado no banco de dados a partir do login: " + principalValue));
            }
        }

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível obter o ID do usuário autenticado.");
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RunningSessionResponseDto> createSession(
            @Valid @RequestBody RunningSessionCreationRequestDto requestDto) {

        RunningSessionResponseDto responseDto = sessionService.createSession(getAuthenticatedUserId(), requestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.id())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<RunningSessionResponseDto> getAllSessionsForUser() {
        return sessionService.getAllSessionsByUserId(getAuthenticatedUserId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR') or @sessionService.isSessionOwner(#id, getAuthenticatedUserId())")
    public ResponseEntity<RunningSessionResponseDto> getSessionById(@PathVariable Long id) {
        RunningSessionResponseDto responseDto = sessionService.getSessionById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR') or @sessionService.isSessionOwner(#id, getAuthenticatedUserId())")
    public ResponseEntity<RunningSessionResponseDto> updateSession(
            @PathVariable Long id,
            @Valid @RequestBody RunningSessionUpdateRequestDto requestDto) {

        RunningSessionResponseDto responseDto = sessionService.updateSession(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR') or @sessionService.isSessionOwner(#id, getAuthenticatedUserId())")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id, getAuthenticatedUserId());
        return ResponseEntity.noContent().build();
    }
}
