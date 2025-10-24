package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBiometrics extends JpaRepository<com.rununit.rununit.domain.entities.UserBiometrics, Long> {


    Optional<com.rununit.rununit.domain.entities.UserBiometrics> findById(Long userId);


    Optional<com.rununit.rununit.domain.entities.UserBiometrics> findByUser(User user);
}
