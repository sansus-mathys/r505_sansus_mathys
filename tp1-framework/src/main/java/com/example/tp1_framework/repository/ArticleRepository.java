package com.example.tp1_framework.repository;

import com.example.tp1_framework.model.Article;
import com.example.tp1_framework.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByAuthor(User author);
}

