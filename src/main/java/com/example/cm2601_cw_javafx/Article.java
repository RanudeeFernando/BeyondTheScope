package com.example.cm2601_cw_javafx;

import java.sql.Timestamp;

public class Article {
    private String articleID; // Unique identifier for the article
    private String title;     // Title of the article
    private String content;   // Full content of the article
    private Category category; // Category determined by NLP
    private String author;    // Author of the article
    private String source;    // Source from which the article was fetched
    private String url;       // URL of the article
    private Timestamp publishedDate; // Published date of the article

    // Constructor
    public Article(String articleID, String title, String content, Category category, String author, String source, String url, Timestamp publishedDate) {
        this.articleID = articleID;
        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;
        this.source = source;
        this.url = url;
        this.publishedDate = publishedDate;
    }

    // Getters and setters
    public String getArticleID() {
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

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPublishedDate(Timestamp publishedDate) {
        this.publishedDate = publishedDate;
    }

    // Optionally, override toString() for better display
    @Override
    public String toString() {
        return "Article{articleID='" + articleID + '\'' +
                ", title='" + title + '\'' +
                ", category=" + category +
                ", author='" + author + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                ", publishedDate=" + publishedDate +
                '}';
    }
}

