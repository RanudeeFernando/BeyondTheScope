package com.example.cm2601_cw_javafx;

import java.sql.Timestamp;

public class UserViewedArticle {
    private Article article;    // The article that was viewed
    private Timestamp viewedAt; // When the user viewed the article

    // Constructor
    public UserViewedArticle(Article article, Timestamp viewedAt) {
        this.article = article;
        this.viewedAt = viewedAt;
    }

    // Getters
    public Article getArticle() {
        return article;
    }

    public Timestamp getViewedAt() {
        return viewedAt;
    }

    // Method to represent this data in a simplified form for ListView
    public String toListViewString() {
        return "Title: " + article.getTitle() + " | Viewed At: " + viewedAt;
    }
}
