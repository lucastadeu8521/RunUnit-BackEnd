package com.rununit.rununit.web.controllers;

import com.rununit.rununit.domain.services.BlogPostService;
import com.rununit.rununit.web.dto.blog.BlogPostResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog/posts")
public class BlogPostPublicController {

    private final BlogPostService postService;

    public BlogPostPublicController(BlogPostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<BlogPostResponseDto> getPublishedPosts() {
        return postService.getPublishedPosts();
    }

    @GetMapping("/{id}")
    public BlogPostResponseDto getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }
}