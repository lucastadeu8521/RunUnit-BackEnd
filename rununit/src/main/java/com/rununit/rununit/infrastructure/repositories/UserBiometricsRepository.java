package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.UserBiometrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBiometricsRepository extends JpaRepository<UserBiometrics, Long> {

}