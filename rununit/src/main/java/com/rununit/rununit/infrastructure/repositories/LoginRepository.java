package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {

    boolean existsByEmail(String email);

    Optional<Login> findByEmail(String email);
}