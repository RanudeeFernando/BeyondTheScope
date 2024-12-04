package com.example.cm2601_cw_javafx.model;

import java.sql.Timestamp;

public class ViewedArticle extends Article {
    private final Timestamp viewedAt;

    // Constructor
    public ViewedArticle(int articleID, String title, Timestamp viewedAt) {
        super(articleID, title);
        this.viewedAt = viewedAt;
    }

    public String toString() {
        return "Title: " + getTitle() + " | Viewed At: " + viewedAt;
    }
}

