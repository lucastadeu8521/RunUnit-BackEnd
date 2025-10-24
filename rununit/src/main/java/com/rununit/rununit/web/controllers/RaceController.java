package com.rununit.rununit.web.controllers;

import com.rununit.rununit.domain.services.RaceService;
import com.rununit.rununit.web.dto.race.RaceCreationRequestDto;
import com.rununit.rununit.web.dto.race.RaceResponseDto;
import com.rununit.rununit.web.dto.race.RaceUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/races")
public class RaceController {

    private final RaceService raceService;

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    @PostMapping
    public ResponseEntity<RaceResponseDto> createRace(@Valid @RequestBody RaceCreationRequestDto requestDto) {
        RaceResponseDto responseDto = raceService.createRace(requestDto);

        // Cria a URI do novo recurso: /api/races/{id}
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.id())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping
    public List<RaceResponseDto> getAllRaces() {
        return raceService.findAllRaces();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RaceResponseDto> getRaceById(@PathVariable Long id) {
        RaceResponseDto responseDto = raceService.findRaceById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RaceResponseDto> updateRace(
            @PathVariable Long id,
            @Valid @RequestBody RaceUpdateRequestDto requestDto) {

        RaceResponseDto responseDto = raceService.updateRace(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRace(@PathVariable Long id) {
        raceService.deleteRace(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<RaceResponseDto> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate
    ) {
        return raceService.searchRacesByFilters(name, location, startDate, endDate);
    }
}