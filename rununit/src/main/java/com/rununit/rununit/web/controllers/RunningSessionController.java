package com.rununit.rununit.web.controllers;

import com.rununit.rununit.domain.services.RunningSessionService;
import com.rununit.rununit.web.dto.runningsession.RunningSessionCreationRequestDto;
import com.rununit.rununit.web.dto.runningsession.RunningSessionResponseDto;
import com.rununit.rununit.web.dto.runningsession.RunningSessionUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class RunningSessionController {

    private final RunningSessionService sessionService;

    public RunningSessionController(RunningSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private Long getAuthenticatedUserId() {
        return 1L;
    }

    @PostMapping
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

    @GetMapping("/my")
    public List<RunningSessionResponseDto> getAllSessionsForUser() {
        return sessionService.getAllSessionsByUserId(getAuthenticatedUserId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RunningSessionResponseDto> getSessionById(@PathVariable Long id) {
        RunningSessionResponseDto responseDto = sessionService.getSessionById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RunningSessionResponseDto> updateSession(
            @PathVariable Long id,
            @Valid @RequestBody RunningSessionUpdateRequestDto requestDto) {

        RunningSessionResponseDto responseDto = sessionService.updateSession(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id, getAuthenticatedUserId());
        return ResponseEntity.noContent().build();
    }
}