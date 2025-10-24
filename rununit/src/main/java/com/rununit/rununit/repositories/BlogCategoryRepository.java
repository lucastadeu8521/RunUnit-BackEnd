package com.rununit.rununit.repositories;

import com.rununit.rununit.entities.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Long> {

    Optional<BlogCategory> findByName(String name);
    Optional<BlogCategory> findBySlug(String slug);
    List<BlogCategory> findByActiveTrue();
}
