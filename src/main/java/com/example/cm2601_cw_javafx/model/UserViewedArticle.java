package com.example.cm2601_cw_javafx.model;

import java.sql.Timestamp;

public class UserViewedArticle extends Article {

    private Timestamp viewedAt; // When the user viewed the article

    // Constructor
    public UserViewedArticle(int articleID, String title, String content, Category category, String author, String source, String url, Timestamp publishedDate, Timestamp viewedAt) {
        super(articleID, title, content, category, author, source, url, publishedDate);
        this.viewedAt = viewedAt;
    }


    // Method to represent this data in a simplified form for ListView
    public String toListViewString() {
        return "Title: " + getTitle() + " | Viewed At: " + viewedAt;
    }
}
