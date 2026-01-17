package com.example.tp1_framework.controller;

import com.example.tp1_framework.dto.*;
import com.example.tp1_framework.service.ArticleService;
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

    @PostMapping
    public ArticleResponse create(@RequestBody ArticleCreateRequest req) {
        return articles.create(req);
    }

    @PutMapping("/{id}")
    public ArticleResponse update(@PathVariable Long id, @RequestBody ArticleUpdateRequest req) {
        return articles.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @RequestParam Long authorId) {
        articles.delete(id, authorId);
    }

    @PostMapping("/{id}/like")
    public ArticleResponse like(@PathVariable Long id, @RequestParam Long userId) {
        return articles.like(id, userId);
    }

    @PostMapping("/{id}/dislike")
    public ArticleResponse dislike(@PathVariable Long id, @RequestParam Long userId) {
        return articles.dislike(id, userId);
    }

    @PostMapping("/{id}/unreact")
    public ArticleResponse unreact(@PathVariable Long id, @RequestParam Long userId) {
        return articles.unreact(id, userId);
    }
}
