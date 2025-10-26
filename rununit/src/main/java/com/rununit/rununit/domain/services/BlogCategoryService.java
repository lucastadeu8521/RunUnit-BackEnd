package com.rununit.rununit.domain.services;

import com.rununit.rununit.domain.entities.BlogCategory;
import com.rununit.rununit.infrastructure.repositories.BlogCategoryRepository;
import com.rununit.rununit.web.dto.blog.BlogCategoryCreationDto;
import com.rununit.rununit.web.dto.blog.BlogCategoryResponseDto;
import com.rununit.rununit.web.dto.blog.BlogCategoryUpdateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogCategoryService {

    private final BlogCategoryRepository categoryRepository;

    public BlogCategoryService(BlogCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private BlogCategoryResponseDto toResponseDto(BlogCategory category) {
        return new BlogCategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getActive()
        );
    }

    @Transactional
    public BlogCategoryResponseDto createCategory(BlogCategoryCreationDto dto) {
        if (categoryRepository.existsBySlug(dto.slug())) {
            throw new RuntimeException("Category slug already exists: " + dto.slug());
        }
        if (categoryRepository.existsByName(dto.name())) {
            throw new RuntimeException("Category name already exists: " + dto.name());
        }

        BlogCategory category = BlogCategory.builder()
                .name(dto.name())
                .slug(dto.slug())
                .description(dto.description())
                .active(true)
                .build();

        BlogCategory savedCategory = categoryRepository.save(category);
        return toResponseDto(savedCategory);
    }

    public List<BlogCategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public BlogCategoryResponseDto getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new RuntimeException("BlogCategory not found with ID: " + id));
    }

    @Transactional
    public BlogCategoryResponseDto updateCategory(Long id, BlogCategoryUpdateDto dto) {
        BlogCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BlogCategory not found with ID: " + id));

        if (dto.name() != null && !dto.name().equals(category.getName())) {
            if (categoryRepository.existsByName(dto.name())) {
                throw new RuntimeException("Category name already exists: " + dto.name());
            }
            category.setName(dto.name());
        }

        if (dto.description() != null) {
            category.setDescription(dto.description());
        }

        if (dto.active() != null) {
            category.setActive(dto.active());
        }

        BlogCategory updatedCategory = categoryRepository.save(category);
        return toResponseDto(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("BlogCategory not found with ID: " + id);
        }
        categoryRepository.deleteById(id);
    }
}