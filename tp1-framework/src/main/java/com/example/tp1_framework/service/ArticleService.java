package com.example.tp1_framework.service;

import com.example.tp1_framework.dto.*;
import com.example.tp1_framework.exception.BadRequestException;
import com.example.tp1_framework.exception.NotFoundException;
import com.example.tp1_framework.model.Article;
import com.example.tp1_framework.model.User;
import com.example.tp1_framework.repository.ArticleRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private enum View { ANON, PUBLISHER, MODERATOR }

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
        if (req.content() == null || req.content().isBlank())
            throw new BadRequestException("content is required");

        User author = userService.getEntityByUsername(currentUsername());
        Article saved = articles.save(new Article(author, LocalDateTime.now(), req.content()));
        return toDto(saved);
    }

    @Transactional
    public ArticleResponse update(Long id, ArticleUpdateRequest req) {
        if (req.content() == null || req.content().isBlank())
            throw new BadRequestException("content is required");

        Article article = getEntity(id);

        String me = currentUsername();
        if (!article.getAuthor().getUsername().equals(me)) {
            throw new BadRequestException("Only the author can update this article");
        }

        article.setContent(req.content());
        return toDto(article);
    }

    public void delete(Long id) {
        Article article = getEntity(id);

        if (isModerator()) {
            articles.delete(article);
            return;
        }

        String me = currentUsername();
        if (!article.getAuthor().getUsername().equals(me)) {
            throw new BadRequestException("Only the author can delete this article");
        }

        articles.delete(article);
    }

    @Transactional
    public ArticleResponse like(Long articleId) {
        Article article = getEntity(articleId);
        User me = userService.getEntityByUsername(currentUsername());

        if (article.getAuthor().getId().equals(me.getId())) {
            throw new BadRequestException("Cannot like/dislike your own article");
        }

        article.getDislikedBy().remove(me);
        article.getLikedBy().add(me);
        return toDto(article);
    }

    @Transactional
    public ArticleResponse dislike(Long articleId) {
        Article article = getEntity(articleId);
        User me = userService.getEntityByUsername(currentUsername());

        if (article.getAuthor().getId().equals(me.getId())) {
            throw new BadRequestException("Author cannot like/dislike their own article");
        }

        article.getLikedBy().remove(me);
        article.getDislikedBy().add(me);
        return toDto(article);
    }

    @Transactional
    public ArticleResponse unreact(Long articleId) {
        Article article = getEntity(articleId);
        User me = userService.getEntityByUsername(currentUsername());

        article.getLikedBy().remove(me);
        article.getDislikedBy().remove(me);
        return toDto(article);
    }

    private Article getEntity(Long id) {
        return articles.findById(id).orElseThrow(() -> new NotFoundException("Article " + id + " not found"));
    }

    private ArticleResponse toDto(Article a) {
        View view = currentView();

        if (view == View.ANON) {
            return new ArticleResponse(
                    a.getId(),
                    a.getAuthor().getUsername(),
                    a.getPublishedAt(),
                    a.getContent(),
                    null, null,
                    null, null
            );
        }

        int likeCount = a.getLikedBy().size();
        int dislikeCount = a.getDislikedBy().size();

        if (view == View.PUBLISHER) {
            return new ArticleResponse(
                    a.getId(),
                    a.getAuthor().getUsername(),
                    a.getPublishedAt(),
                    a.getContent(),
                    likeCount,
                    dislikeCount,
                    null,
                    null
            );
        }

        // MODERATOR : tout
        Set<String> liked = a.getLikedBy().stream().map(User::getUsername).collect(Collectors.toSet());
        Set<String> disliked = a.getDislikedBy().stream().map(User::getUsername).collect(Collectors.toSet());

        return new ArticleResponse(
                a.getId(),
                a.getAuthor().getUsername(),
                a.getPublishedAt(),
                a.getContent(),
                likeCount,
                dislikeCount,
                liked,
                disliked
        );
    }

    private View currentView() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) return View.ANON;

        boolean isModerator = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MODERATOR"));
        if (isModerator) return View.MODERATOR;

        return View.PUBLISHER;
    }

    private String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    private boolean isModerator() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MODERATOR"));
    }
}
