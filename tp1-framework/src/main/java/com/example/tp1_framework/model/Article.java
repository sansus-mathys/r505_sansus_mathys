package com.example.tp1_framework.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private LocalDateTime publishedAt;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToMany
    @JoinTable(
            name = "article_likes",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likedBy = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "article_dislikes",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> dislikedBy = new HashSet<>();

    public Article() {}

    public Article(User author, LocalDateTime publishedAt, String content) {
        this.author = author;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public Long getId() { return id; }
    public User getAuthor() { return author; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public String getContent() { return content; }
    public Set<User> getLikedBy() { return likedBy; }
    public Set<User> getDislikedBy() { return dislikedBy; }

    public void setAuthor(User author) { this.author = author; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }
    public void setContent(String content) { this.content = content; }
}
