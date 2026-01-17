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
    public void delete(@PathVariable Long id) {
        articles.delete(id);
    }

    @PutMapping("/{id}/likes/{userId}")
    public ArticleResponse like(@PathVariable Long id, @PathVariable Long userId) {
        return articles.like(id, userId);
    }

    @PutMapping("/{id}/dislikes/{userId}")
    public ArticleResponse dislike(@PathVariable Long id, @PathVariable Long userId) {
        return articles.dislike(id, userId);
    }

    @DeleteMapping("/{id}/reactions/{userId}")
    public ArticleResponse unreact(@PathVariable Long id, @PathVariable Long userId) {
        return articles.unreact(id, userId);
    }
}
