package com.rununit.rununit.web.controllers;

import com.rununit.rununit.domain.services.BlogPostService;
import com.rununit.rununit.web.dto.blog.BlogPostCreationDto;
import com.rununit.rununit.web.dto.blog.BlogPostResponseDto;
import com.rununit.rununit.web.dto.blog.BlogPostUpdateDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/blog-posts")
public class BlogPostAdminController {

    private final BlogPostService postService;

    public BlogPostAdminController(BlogPostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<BlogPostResponseDto> createPost(@Valid @RequestBody BlogPostCreationDto dto) {
        BlogPostResponseDto response = postService.createPost(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<BlogPostResponseDto> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public BlogPostResponseDto getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PutMapping("/{id}")
    public BlogPostResponseDto updatePost(@PathVariable Long id, @Valid @RequestBody BlogPostUpdateDto dto) {
        return postService.updatePost(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}