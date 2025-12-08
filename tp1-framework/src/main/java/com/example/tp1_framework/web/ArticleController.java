package com.example.tp1_framework.web;

import com.example.tp1_framework.model.Article;
import com.example.tp1_framework.model.User;
import com.example.tp1_framework.repository.ArticleRepository;
import com.example.tp1_framework.repository.UserRepository;
import com.example.tp1_framework.web.dto.ArticleRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleController(ArticleRepository articleRepository,
                             UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    // GET /api/articles -> tous les articles
    @GetMapping
    public List<Article> getAll() {
        return articleRepository.findAll();
    }

    // GET /api/articles/{id} -> un article par id
    @GetMapping("/{id}")
    public ResponseEntity<Article> getById(@PathVariable Long id) {
        return articleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/articles/author/{authorId} -> tous les articles d'un auteur (User)
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Article>> getByAuthor(@PathVariable Long authorId) {
        return userRepository.findById(authorId)
                .map(user -> ResponseEntity.ok(articleRepository.findByAuthor(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/articles -> créer un article
    @PostMapping
    public ResponseEntity<Article> create(@RequestBody ArticleRequest request) {
        User author = userRepository.findById(request.getAuthorId())
                .orElse(null);

        if (author == null) {
            return ResponseEntity.badRequest().build(); // auteur inconnu
        }

        Article article = new Article();
        article.setAuthor(author);
        article.setContent(request.getContent());
        article.setPublishedAt(LocalDateTime.now());

        Article saved = articleRepository.save(article);
        return ResponseEntity.ok(saved);
    }

    // PUT /api/articles/{id} -> modifier le contenu (et éventuellement l'auteur)
    @PutMapping("/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id,
                                          @RequestBody ArticleRequest request) {
        Article existing = articleRepository.findById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        if (request.getAuthorId() != null) {
            User author = userRepository.findById(request.getAuthorId()).orElse(null);
            if (author == null) {
                return ResponseEntity.badRequest().build();
            }
            existing.setAuthor(author);
        }
        if (request.getContent() != null) {
            existing.setContent(request.getContent());
        }

        Article saved = articleRepository.save(existing);
        return ResponseEntity.ok(saved);
    }


    // DELETE /api/articles/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!articleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        articleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------
    // Gestion des likes / dislikes (sans auth)
    // -------------------

    // POST /api/articles/{id}/like?userId=...
    @PostMapping("/{id}/like")
    public ResponseEntity<Article> like(@PathVariable Long id, @RequestParam Long userId) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        article.getDislikedBy().remove(user);
        article.getLikedBy().add(user);

        Article saved = articleRepository.save(article);
        return ResponseEntity.ok(saved);
    }

    // POST /api/articles/{id}/dislike?userId=...
    @PostMapping("/{id}/dislike")
    public ResponseEntity<Article> dislike(@PathVariable Long id, @RequestParam Long userId) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        article.getLikedBy().remove(user);
        article.getDislikedBy().add(user);

        Article saved = articleRepository.save(article);
        return ResponseEntity.ok(saved);
    }

    // facultatif : annuler like/dislike
    @PostMapping("/{id}/unreact")
    public ResponseEntity<Article> removeReaction(@PathVariable Long id, @RequestParam Long userId) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        article.getLikedBy().remove(user);
        article.getDislikedBy().remove(user);

        Article saved = articleRepository.save(article);
        return ResponseEntity.ok(saved);
    }
}

