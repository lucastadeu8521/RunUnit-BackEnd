package com.rununit.rununit.repositories;

import com.rununit.rununit.entities.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembershipTypeRepository extends JpaRepository<MembershipType, Short> {


    Optional<MembershipType> findByName(String name);
}

