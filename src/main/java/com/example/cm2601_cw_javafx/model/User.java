package com.example.cm2601_cw_javafx.model;

import com.example.cm2601_cw_javafx.db.DBManager;
import java.sql.SQLException;
import java.util.*;

public class User extends SystemUser {
    // Lists to store user interactions and preferences
    private List<Category> selectedCategories;
    private List<ViewedArticle> viewedArticles;
    private List<Article> likedArticles;
    private List<Article> skippedArticles;


    // Constructor initializing user with ID, username, and password
    public User(int userID, String username, String password) {
        super(userID, username, password);
        this.selectedCategories = new ArrayList<>();
        this.viewedArticles = new ArrayList<>();
        this.likedArticles = new ArrayList<>();
        this.skippedArticles = new ArrayList<>();

    }

    // Constructor with selectedCategories parameter
    public User(int userID, String username, String password, List<Category> selectedCategories) {
        super(userID, username, password);
        this.selectedCategories = selectedCategories;
    }

    // Get liked articles
    public List<Article> getLikedArticles(int userID) throws SQLException {
        likedArticles = DBManager.getArticlesByInteractionQuery(userID, "LIKE");
        return likedArticles;
    }

    // Get skipped articles
    public List<Article> getSkippedArticles(int userID) throws SQLException {
        skippedArticles = DBManager.getArticlesByInteractionQuery(userID, "SKIP");
        return skippedArticles;
    }

    // get viewed articles
    public List<ViewedArticle> getViewedArticles(int userID) {
        viewedArticles = DBManager.getViewedArticlesQuery(userID);
        return viewedArticles;
    }

    // Method to check if the user has liked article
    public boolean hasLikedArticle(Article article) {
        for (Article a : likedArticles) {
            if (a.getArticleID() == article.getArticleID()) {
                return true; // Found in the in-memory list
            }
        }

        // Check the database for past interactions if not found in the list
        return DBManager.findInteractionQuery(getUserID(), article.getArticleID(), "LIKE");

    }

    // Method to check if the user has skipped article
    public boolean hasSkippedArticle(Article article) {
        for (Article a : skippedArticles) {
            if (a.getArticleID() == article.getArticleID()) {
                return true; // Found in the in-memory list
            }
        }

        // Check the database for past interactions if not found in the list
        return DBManager.findInteractionQuery(getUserID(), article.getArticleID(), "SKIP");

    }

    // Add article to liked list and update database
    public void likeArticle(Article article) {
        boolean isInLikedList = false;
        for (Article a : likedArticles) {
            if (a.getArticleID() == article.getArticleID()) {
                isInLikedList = true;
                break;
            }
        }

        if (!isInLikedList) {
            likedArticles.add(article);
            DBManager.addInteractionQuery(getUserID(), article.getArticleID(), "LIKE");
        }
    }

    // Remove article from liked list and update database
    public void unlikeArticle(Article article) {
        for (int i = 0; i < likedArticles.size(); i++) {
            if (likedArticles.get(i).getArticleID() == article.getArticleID()) {
                likedArticles.remove(i);
                break;
            }
        }

        DBManager.removeInteractionQuery(getUserID(), article.getArticleID());
    }

    // Add article to skipped list and update the database
    public void skipArticle(Article article) {
        boolean isInSkippedList = false;
        for (Article a : skippedArticles) {
            if (a.getArticleID() == article.getArticleID()) {
                isInSkippedList = true;
                break;
            }
        }

        if (!isInSkippedList) {
            skippedArticles.add(article);

            DBManager.addInteractionQuery(getUserID(), article.getArticleID(), "SKIP");
        }
    }

    // Remove article from skipped list and update database
    public void unskipArticle(Article article) {
        for (int i = 0; i < skippedArticles.size(); i++) {
            if (skippedArticles.get(i).getArticleID() == article.getArticleID()) {
                skippedArticles.remove(i);
                break;
            }
        }

        DBManager.removeInteractionQuery(getUserID(), article.getArticleID());
    }

    // get selected categories from database
    public List<Category> getSelectedCategories(int userID) {
        try {
            selectedCategories = DBManager.getUserCategoriesQuery(userID);
        } catch (SQLException e) {
            System.out.println("Error retrieving selected categories for user: " + getUsername());

        }
        return selectedCategories;
    }

    // Update user's selected categories in memory and in database
    public void updateSelectedCategories(int userID, List<Category> newCategories) {
        try {
            DBManager.updateUserCategoriesQuery(userID, newCategories);

            selectedCategories.clear();
            selectedCategories.addAll(newCategories);

        } catch (SQLException e) {
            System.out.println("Error updating selected categories for user: " + getUsername());

        }
    }

    // Update user's password in memory and in database
    public void updatePassword(int userID, String newPassword) {
        try {
            DBManager.updatePasswordQuery(userID, newPassword);
            setPassword(newPassword);

        } catch (SQLException e) {
            System.out.println("Error updating password for user: " + getUsername());
            e.printStackTrace();
        }
    }


}


