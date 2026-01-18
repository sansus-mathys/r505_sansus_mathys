package com.example.tp1_framework.controller;

import com.example.tp1_framework.dto.*;
import com.example.tp1_framework.service.ArticleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articles;

    public ArticleController(ArticleService articles) {
        this.articles = articles;
    }

    @GetMapping
    public List<ArticleResponse> list() {
        return articles.list();
    }

    @GetMapping("/{id}")
    public ArticleResponse get(@PathVariable Long id) {
        return articles.get(id);
    }

    @PreAuthorize("hasRole('PUBLISHER')")
    @PostMapping
    public ArticleResponse create(@RequestBody ArticleCreateRequest req) {
        return articles.create(req);
    }

    @PreAuthorize("hasRole('PUBLISHER')")
    @PutMapping("/{id}")
    public ArticleResponse update(@PathVariable Long id, @RequestBody ArticleUpdateRequest req) {
        return articles.update(id, req);
    }

    @PreAuthorize("hasAnyRole('PUBLISHER','MODERATOR')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        articles.delete(id);
    }

    @PreAuthorize("hasRole('PUBLISHER')")
    @PutMapping("/{id}/likes")
    public ArticleResponse like(@PathVariable Long id) {
        return articles.like(id);
    }

    @PreAuthorize("hasRole('PUBLISHER')")
    @PutMapping("/{id}/dislikes")
    public ArticleResponse dislike(@PathVariable Long id) {
        return articles.dislike(id);
    }

    @PreAuthorize("hasRole('PUBLISHER')")
    @DeleteMapping("/{id}/reactions")
    public ArticleResponse unreact(@PathVariable Long id) {
        return articles.unreact(id);
    }
}
