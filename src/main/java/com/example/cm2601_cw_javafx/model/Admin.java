package com.example.cm2601_cw_javafx.model;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.service.ArticleFetcher;

import java.util.List;

public class Admin extends SystemUser {

    public Admin(int userID, String username, String password) {
        super(userID, username, password);

    }

    public Admin(int userID, String username){
        super(userID, username);

    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    public boolean deleteArticle(String articleID) {
        if (articleID == null || articleID.trim().isEmpty()) {
            throw new IllegalArgumentException("Article ID cannot be null or empty.");
        }

        try {
            int articleIDInt = Integer.parseInt(articleID.trim());
            return DBManager.deleteArticleByID(articleIDInt);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Article ID. Please enter a numeric value.");
        }
    }

    public void updateArticleCategory(Article article) {
        try {
            DBManager.updateArticleCategoryInDatabase(article); // Static method call
            System.out.println("Article category updated successfully for: " + article.getTitle());
        } catch (Exception e) {
            System.out.println("Error updating category for article: " + article.getTitle());

        }
    }

    public List<Article> fetchArticlesManually() {
        return ArticleFetcher.fetchArticles(); // Returns the list of fetched articles
    }



}
