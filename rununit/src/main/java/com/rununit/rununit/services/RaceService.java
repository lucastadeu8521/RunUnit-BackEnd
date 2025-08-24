package com.rununit.rununit.services;

import com.rununit.rununit.entities.Race;
import com.rununit.rununit.repositories.RaceRepository;
import com.rununit.rununit.services.exceptions.DatabaseException;
import com.rununit.rununit.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RaceService {

    @Autowired
    private RaceRepository repository;

    public List<Race> findAll() {
        return repository.findAll();
    }

    public Race findById(UUID id) {
        Optional<Race> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Race insert(Race obj) {
        return repository.save(obj);
    }

    public void deleteById(UUID id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public Race update(UUID id, Race obj) {

        try {
            Race entity = repository.getReferenceById(id);

            updateData(entity, obj);

            return repository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    public void updateData(Race entity, Race obj) {
        entity.setName(obj.getName());
        entity.setDescription(obj.getDescription());
        entity.setLocation(obj.getLocation());
        entity.setEndTime(obj.getEndTime());
        entity.setStartTime(obj.getStartTime());
    }

    public List<Race> searchRacesByFilters(String name, String location, ZonedDateTime startDate, ZonedDateTime endDate) {
        return repository.findByFilters(name, location, startDate, endDate);
    }
}
