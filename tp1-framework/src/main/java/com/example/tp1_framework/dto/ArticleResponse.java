package com.example.tp1_framework.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record ArticleResponse(
        Long id,
        String authorUsername,
        LocalDateTime publishedAt,
        String content,
        int likeCount,
        int dislikeCount,
        Set<String> likedBy,      // usernames
        Set<String> dislikedBy    // usernames
) {}
