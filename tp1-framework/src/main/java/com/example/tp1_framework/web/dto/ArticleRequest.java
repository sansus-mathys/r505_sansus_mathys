package com.example.tp1_framework.web.dto;

public class ArticleRequest {

    private Long authorId;
    private String content;

    public ArticleRequest() {
    }

    public ArticleRequest(Long authorId, String content) {
        this.authorId = authorId;
        this.content = content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
