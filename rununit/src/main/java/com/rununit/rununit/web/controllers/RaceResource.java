package com.rununit.rununit.web.controllers;

import com.rununit.rununit.domain.entities.Race;
import com.rununit.rununit.domain.services.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/races")
public class RaceResource {

    @Autowired
    private RaceService service;

    @GetMapping
    public ResponseEntity<List<Race>> findAll() {
        List<Race> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Race> findById(@PathVariable UUID id) {
        Race obj = service.findById(id);

        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Race> insert(@RequestBody Race obj) {
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
                buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Race> update(@PathVariable UUID id, @RequestBody Race obj) {
        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<Race>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) ZonedDateTime startDate,
            @RequestParam(required = false) ZonedDateTime endDate) {

        List<Race> list = service.searchRacesByFilters(name, location, startDate, endDate);
        return ResponseEntity.ok().body(list);
    }

}
