package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembershipTypeRepository extends JpaRepository<MembershipType, Long> {

    Optional<MembershipType> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}