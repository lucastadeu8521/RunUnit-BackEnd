package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.BlogPost;
import com.rununit.rununit.domain.entities.PostCategory;
import com.rununit.rununit.domain.entities.PostCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, PostCategoryId> {

    void deleteByPost(BlogPost post);
}