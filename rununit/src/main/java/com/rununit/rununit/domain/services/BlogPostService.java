package com.rununit.rununit.domain.services;

import com.rununit.rununit.domain.entities.BlogCategory;
import com.rununit.rununit.domain.entities.BlogPost;
import com.rununit.rununit.domain.entities.PostCategory;
import com.rununit.rununit.domain.entities.PostCategoryId;
import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.domain.enums.PostStatus;
import com.rununit.rununit.infrastructure.repositories.BlogCategoryRepository;
import com.rununit.rununit.infrastructure.repositories.BlogPostRepository;
import com.rununit.rununit.infrastructure.repositories.PostCategoryRepository;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import com.rununit.rununit.web.dto.blog.BlogCategoryResponseDto;
import com.rununit.rununit.web.dto.blog.BlogPostCreationDto;
import com.rununit.rununit.web.dto.blog.BlogPostResponseDto;
import com.rununit.rununit.web.dto.blog.BlogPostUpdateDto;
import com.rununit.rununit.web.dto.blog.UserSimpleResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogPostService {

    private final BlogPostRepository postRepository;
    private final UserRepository userRepository;
    private final BlogCategoryRepository categoryRepository;
    private final PostCategoryRepository postCategoryRepository;

    public BlogPostService(BlogPostRepository postRepository, UserRepository userRepository, BlogCategoryRepository categoryRepository, PostCategoryRepository postCategoryRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.postCategoryRepository = postCategoryRepository;
    }

    private UserSimpleResponseDto toUserSimpleDto(User user) {
        return new UserSimpleResponseDto(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getProfilePictureUrl()
        );
    }

    private BlogCategoryResponseDto toCategoryResponseDto(BlogCategory category) {
        return new BlogCategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getActive()
        );
    }

    private BlogPostResponseDto toResponseDto(BlogPost post) {
        List<BlogCategoryResponseDto> categoryDtos = post.getCategories().stream()
                .map(pc -> toCategoryResponseDto(pc.getCategory()))
                .collect(Collectors.toList());

        return new BlogPostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getSlug(),
                post.getExcerpt(),
                post.getThumbnailUrl(),
                post.getContent(),
                post.getStatus(),
                post.getPublicationDate(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                toUserSimpleDto(post.getAuthor()),
                categoryDtos
        );
    }

    private void updatePostCategories(BlogPost post, List<Long> categoryIds) {
        if (categoryIds == null) return;

        postCategoryRepository.deleteByPost(post);
        post.getCategories().clear();

        List<PostCategory> newAssociations = new ArrayList<>();

        if (!categoryIds.isEmpty()) {
            List<BlogCategory> categories = categoryRepository.findAllById(categoryIds);

            if (categories.size() != categoryIds.size()) {
                throw new RuntimeException("One or more category IDs are invalid.");
            }

            for (BlogCategory category : categories) {
                PostCategoryId id = new PostCategoryId(post.getId(), category.getId());
                PostCategory association = PostCategory.builder()
                        .id(id)
                        .post(post)
                        .category(category)
                        .build();
                newAssociations.add(association);
            }
            postCategoryRepository.saveAll(newAssociations);
            post.setCategories(newAssociations);
        }
    }

    @Transactional
    public BlogPostResponseDto createPost(BlogPostCreationDto dto) {
        if (postRepository.existsBySlug(dto.slug())) {
            throw new RuntimeException("Post slug already exists: " + dto.slug());
        }
        if (postRepository.existsByTitle(dto.title())) {
            throw new RuntimeException("Post title already exists: " + dto.title());
        }

        User author = userRepository.findById(dto.authorId())
                .orElseThrow(() -> new RuntimeException("Author not found with ID: " + dto.authorId()));

        BlogPost newPost = BlogPost.builder()
                .author(author)
                .title(dto.title())
                .slug(dto.slug())
                .content(dto.content())
                .excerpt(dto.excerpt())
                .thumbnailUrl(dto.thumbnailUrl())
                .status(PostStatus.DRAFT)
                .categories(new ArrayList<>())
                .build();

        BlogPost savedPost = postRepository.save(newPost);
        updatePostCategories(savedPost, dto.categoryIds());

        return toResponseDto(savedPost);
    }

    public List<BlogPostResponseDto> getPublishedPosts() {
        return postRepository.findAllByStatusOrderByPublicationDateDesc(PostStatus.PUBLISHED).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<BlogPostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public BlogPostResponseDto getPostById(Long id) {
        return postRepository.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new RuntimeException("BlogPost not found with ID: " + id));
    }

    @Transactional
    public BlogPostResponseDto updatePost(Long postId, BlogPostUpdateDto dto) {
        BlogPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("BlogPost not found with ID: " + postId));

        if (dto.title() != null && !dto.title().equals(post.getTitle())) {
            if (postRepository.existsByTitle(dto.title())) {
                throw new RuntimeException("Post title already exists: " + dto.title());
            }
            post.setTitle(dto.title());
        }
        if (dto.content() != null) post.setContent(dto.content());
        if (dto.excerpt() != null) post.setExcerpt(dto.excerpt());
        if (dto.thumbnailUrl() != null) post.setThumbnailUrl(dto.thumbnailUrl());

        if (dto.status() != null && dto.status() != post.getStatus()) {
            post.setStatus(dto.status());
            if (dto.status() == PostStatus.PUBLISHED && post.getPublicationDate() == null) {
                post.setPublicationDate(Instant.now());
            }
        }

        if (dto.publicationDate() != null) {
            post.setPublicationDate(dto.publicationDate());
        }

        if (dto.categoryIds() != null) {
            updatePostCategories(post, dto.categoryIds());
        }

        BlogPost updatedPost = postRepository.save(post);
        return toResponseDto(updatedPost);
    }

    @Transactional
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("BlogPost not found with ID: " + postId);
        }
        postRepository.deleteById(postId);
    }
}