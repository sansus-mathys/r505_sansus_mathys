package com.example.tp1_framework.dto;

public record ArticleCreateRequest(
        Long authorId,
        String content
) {}
