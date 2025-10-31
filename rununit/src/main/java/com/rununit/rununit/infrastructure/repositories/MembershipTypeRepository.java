package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importação necessária

@Repository
public interface MembershipTypeRepository extends JpaRepository<MembershipType, Long> {


    boolean existsByNameIgnoreCase(String name);


    Optional<MembershipType> findByNameIgnoreCase(String name);
}