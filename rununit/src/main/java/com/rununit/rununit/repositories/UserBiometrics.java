package com.rununit.rununit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBiometrics extends JpaRepository<com.rununit.rununit.entities.UserBiometrics, Long> {


    Optional<com.rununit.rununit.entities.UserBiometrics> findById(Long userId);


    Optional<com.rununit.rununit.entities.UserBiometrics> findByUser(com.rununit.rununit.entities.User user);
}
