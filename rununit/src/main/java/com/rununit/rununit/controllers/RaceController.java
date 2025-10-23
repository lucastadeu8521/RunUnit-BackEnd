package com.rununit.rununit.controllers;

import com.rununit.rununit.entities.Race;
import com.rununit.rununit.services.RaceService;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/races")
public class RaceController {

    private final RaceService service;

    public RaceController(RaceService service) {
        this.service = service;
    }


    @GetMapping
    public List<Race> getAll() {
        return service.findAll();
    }


    @GetMapping("/{id}")
    public Race getById(@PathVariable UUID id) {
        return service.findById(id); // já lança ResourceNotFoundException se não encontrar
    }


    @PostMapping
    public Race create(@RequestBody Race race) {
        return service.insert(race);
    }


    @PutMapping("/{id}")
    public Race update(@PathVariable UUID id, @RequestBody Race race) {
        return service.update(id, race);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.deleteById(id);
    }


    @GetMapping("/search")
    public List<Race> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) ZonedDateTime startDate,
            @RequestParam(required = false) ZonedDateTime endDate
    ) {
        return service.searchRacesByFilters(name, location, startDate, endDate);
    }
}
