package com.rununit.rununit.repositories;

import com.rununit.rununit.entities.PostCategory;
import com.rununit.rununit.entities.PostCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, PostCategoryId> {


    List<PostCategory> findByPostId(Long postId);

    List<PostCategory> findByCategoryId(Long categoryId);
}
