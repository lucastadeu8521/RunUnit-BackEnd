package com.rununit.rununit.infrastructure.repositories;

import com.rununit.rununit.domain.entities.PostCategory;
import com.rununit.rununit.domain.entities.PostCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, PostCategoryId> {


    List<PostCategory> findByPostId(Long postId);

    List<PostCategory> findByCategoryId(Long categoryId);
}
