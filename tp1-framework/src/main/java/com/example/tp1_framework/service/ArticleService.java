package com.example.tp1_framework.service;

import com.example.tp1_framework.dto.*;
import com.example.tp1_framework.exception.BadRequestException;
import com.example.tp1_framework.exception.NotFoundException;
import com.example.tp1_framework.model.Article;
import com.example.tp1_framework.model.User;
import com.example.tp1_framework.repository.ArticleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articles;
    private final UserService userService;

    public ArticleService(ArticleRepository articles, UserService userService) {
        this.articles = articles;
        this.userService = userService;
    }

    public List<ArticleResponse> list() {
        return articles.findAll().stream().map(this::toDto).toList();
    }

    public ArticleResponse get(Long id) {
        return toDto(getEntity(id));
    }

    public ArticleResponse create(ArticleCreateRequest req) {
        if (req.authorId() == null) throw new BadRequestException("authorId is required");
        if (req.content() == null || req.content().isBlank()) throw new BadRequestException("content is required");

        User author = userService.getEntityById(req.authorId());
        Article saved = articles.save(new Article(author, LocalDateTime.now(), req.content()));
        return toDto(saved);
    }

    @Transactional
    public ArticleResponse update(Long id, ArticleUpdateRequest req) {
        if (req.authorId() == null) throw new BadRequestException("authorId is required");
        if (req.content() == null || req.content().isBlank()) throw new BadRequestException("content is required");

        Article article = getEntity(id);

        // “Auteur uniquement” simulé sans auth : on demande authorId dans la requête.
        if (!article.getAuthor().getId().equals(req.authorId())) {
            throw new BadRequestException("Only the author can update this article (authorId mismatch)");
        }

        article.setContent(req.content());
        return toDto(article);
    }

    public void delete(Long id, Long authorId) {
        if (authorId == null) throw new BadRequestException("authorId is required");
        Article article = getEntity(id);

        if (!article.getAuthor().getId().equals(authorId)) {
            throw new BadRequestException("Only the author can delete this article (authorId mismatch)");
        }

        articles.delete(article);
    }

    @Transactional
    public ArticleResponse like(Long articleId, Long userId) {
        Article article = getEntity(articleId);
        User user = userService.getEntityById(userId);

        if (article.getAuthor().getId().equals(userId)) {
            throw new BadRequestException("Author cannot like/dislike their own article");
        }

        article.getDislikedBy().remove(user);
        article.getLikedBy().add(user);

        return toDto(article);
    }

    @Transactional
    public ArticleResponse dislike(Long articleId, Long userId) {
        Article article = getEntity(articleId);
        User user = userService.getEntityById(userId);

        if (article.getAuthor().getId().equals(userId)) {
            throw new BadRequestException("Author cannot like/dislike their own article");
        }

        article.getLikedBy().remove(user);
        article.getDislikedBy().add(user);

        return toDto(article);
    }

    @Transactional
    public ArticleResponse unreact(Long articleId, Long userId) {
        Article article = getEntity(articleId);
        User user = userService.getEntityById(userId);

        article.getLikedBy().remove(user);
        article.getDislikedBy().remove(user);

        return toDto(article);
    }

    private Article getEntity(Long id) {
        return articles.findById(id).orElseThrow(() -> new NotFoundException("Article " + id + " not found"));
    }

    private ArticleResponse toDto(Article a) {
        Set<String> liked = a.getLikedBy().stream().map(User::getUsername).collect(Collectors.toSet());
        Set<String> disliked = a.getDislikedBy().stream().map(User::getUsername).collect(Collectors.toSet());

        return new ArticleResponse(
                a.getId(),
                a.getAuthor().getUsername(),
                a.getPublishedAt(),
                a.getContent(),
                liked.size(),
                disliked.size(),
                liked,
                disliked
        );
    }
}
