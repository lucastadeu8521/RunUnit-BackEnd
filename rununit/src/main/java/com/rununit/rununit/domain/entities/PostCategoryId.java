package com.rununit.rununit.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCategoryId implements Serializable {

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "category_id")
    private Long categoryId;
}