package com.example.cm2601_cw_javafx.model;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.service.ArticleFetcher;

import java.util.List;

public class Admin extends SystemUser {

    public Admin(int userID, String username, String password) {
        super(userID, username, password);

    }


//    public boolean deleteArticle(String articleID) {
//        if (articleID == null || articleID.trim().isEmpty()) {
//            throw new IllegalArgumentException("Article ID cannot be null or empty.");
//        }
//
//        try {
//            int articleIDInt = Integer.parseInt(articleID.trim());
//            return DBManager.deleteArticleByIDQuery(articleIDInt);
//        } catch (NumberFormatException e) {
//            throw new IllegalArgumentException("Invalid Article ID. Please enter a numeric value.");
//        }
//    }

    public boolean deleteArticle(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("Article cannot be null.");
        }

        try {
            int articleID = article.getArticleID(); // Retrieve the article ID from the Article object
            return DBManager.deleteArticleByIDQuery(articleID); // Perform the delete operation in the database
        } catch (Exception e) {
            throw new RuntimeException("Error deleting the article: " + article.getTitle(), e);
        }
    }



    public void updateArticleCategory(Article article) {
        try {
            DBManager.updateArticleCategoryQuery(article);
            System.out.println("Article category updated successfully for: " + article.getTitle());
        } catch (Exception e) {
            System.out.println("Error updating category for article: " + article.getTitle());

        }
    }

    public List<Article> fetchArticlesManually() {
        return ArticleFetcher.fetchArticles();
    }



}
