package com.rununit.rununit.web.controllers;

import com.rununit.rununit.domain.services.BlogCategoryService;
import com.rununit.rununit.web.dto.blog.BlogCategoryCreationDto;
import com.rununit.rununit.web.dto.blog.BlogCategoryResponseDto;
import com.rununit.rununit.web.dto.blog.BlogCategoryUpdateDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/blog-categories")
public class BlogCategoryController {

    private final BlogCategoryService categoryService;

    public BlogCategoryController(BlogCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<BlogCategoryResponseDto> createCategory(@Valid @RequestBody BlogCategoryCreationDto dto) {
        BlogCategoryResponseDto response = categoryService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<BlogCategoryResponseDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public BlogCategoryResponseDto getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PutMapping("/{id}")
    public BlogCategoryResponseDto updateCategory(@PathVariable Long id, @Valid @RequestBody BlogCategoryUpdateDto dto) {
        return categoryService.updateCategory(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}