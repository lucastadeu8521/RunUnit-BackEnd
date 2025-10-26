package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Long> {

    Optional<BlogCategory> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsByName(String name);
}