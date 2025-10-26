package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.BlogPost;
import com.rununit.rununit.domain.enums.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    Optional<BlogPost> findBySlug(String slug);

    boolean existsByTitle(String title);

    boolean existsBySlug(String slug);

    List<BlogPost> findAllByStatusOrderByPublicationDateDesc(PostStatus status);
}