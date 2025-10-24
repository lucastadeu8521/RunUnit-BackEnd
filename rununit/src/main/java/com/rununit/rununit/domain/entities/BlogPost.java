package com.rununit.rununit.domain.entities;

import com.rununit.rununit.domain.enums.PostStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "blog_posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostCategory> categories;

    @Column(name = "title", nullable = false, unique = true, length = 255)
    private String title;

    @Column(name = "slug", nullable = false, unique = true, length = 255)
    private String slug;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "excerpt", length = 500)
    private String excerpt;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private PostStatus status = PostStatus.DRAFT;

    @Column(name = "publication_date")
    private Instant publicationDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}