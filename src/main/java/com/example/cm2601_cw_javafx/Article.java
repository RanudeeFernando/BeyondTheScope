package com.example.cm2601_cw_javafx;

public class Article {
    private String articleID; // Unique identifier for the article
    private String title; // Title of the article
    private String content; // Full content of the article
    private Category category; // Category determined by NLP
    private String author; // Author of the article
    private String source; // Source from which the article was fetched

    public Article(String articleID, String title, String content, Category category, String author, String source) {
        this.articleID = articleID;
        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;
        this.source = source;
    }

    // Getters and setters for article attributes
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

    // Optionally, you can override toString() for better display
    @Override
    public String toString() {
        return STR."Article{articleID='\{articleID}\{'\''}, title='\{title}\{'\''}, category=\{category}, author='\{author}\{'\''}, source='\{source}\{'\''}\{'}'}";
    }

}
