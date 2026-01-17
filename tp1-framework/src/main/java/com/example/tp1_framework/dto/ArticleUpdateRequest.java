package com.example.tp1_framework.dto;

public record ArticleUpdateRequest(
        Long authorId,
        String content
) {}
