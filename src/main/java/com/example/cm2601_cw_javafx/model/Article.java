package com.example.cm2601_cw_javafx.model;

import java.sql.Timestamp;

public class Article {
    private int articleID;
    private final String title;
    private String content;
    private Category category;
    private String author;
    private String source;
    private String url;
    private Timestamp publishedDate;

    // Constructor
    public Article(int articleID, String title, String content, Category category, String author, String source, String url, Timestamp publishedDate) {
        this.articleID = articleID;
        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;
        this.source = source;
        this.url = url;
        this.publishedDate = publishedDate;
    }


    public Article(int articleID, String title){
        this.articleID = articleID;
        this.title = title;
    }

    // Getters and setters
    public int getArticleID() {
        return articleID;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Category getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }

    public String getSource() {
        return source;
    }

    public String getUrl() {
        return url;
    }

    public Timestamp getPublishedDate() {
        return publishedDate;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    // Optionally, override toString() for better display
    @Override
    public String toString() {
        return "Article {articleID='" + articleID + '\'' +
                ", title='" + title + '\'' +
                ", category=" + category +
                ", author='" + author + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                ", publishedDate=" + publishedDate +
                '}';
    }
}

