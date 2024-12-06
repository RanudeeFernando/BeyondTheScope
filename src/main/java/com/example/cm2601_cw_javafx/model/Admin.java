package com.example.cm2601_cw_javafx.model;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.service.ArticleFetcher;
import java.util.List;

// Represents admin user with privileges to manage articles
public class Admin extends SystemUser {

    // Constructor
    public Admin(int userID, String username, String password) {
        super(userID, username, password);

    }

    // Deletes an article from the database
    public boolean deleteArticle(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("Article cannot be null.");
        }

        try {
            int articleID = article.getArticleID();
            return DBManager.deleteArticleByIDQuery(articleID);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting the article: " + article.getTitle(), e);
        }
    }


    // Updates the category of an article
    public void updateArticleCategory(Article article) {
        try {
            DBManager.updateArticleCategoryQuery(article);
            System.out.println("Article category updated successfully for: " + article.getTitle());
        } catch (Exception e) {
            System.out.println("Error updating category for article: " + article.getTitle());

        }
    }

    // Fetches articles manually
    public List<Article> fetchArticlesManually() {
        return ArticleFetcher.fetchArticles();
    }



}
