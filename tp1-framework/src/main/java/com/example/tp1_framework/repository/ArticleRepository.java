package com.example.tp1_framework.repository;

import com.example.tp1_framework.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
