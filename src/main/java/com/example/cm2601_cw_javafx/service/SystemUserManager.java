package com.example.cm2601_cw_javafx.service;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Category;
import com.example.cm2601_cw_javafx.model.Article;
import com.example.cm2601_cw_javafx.model.SystemUser;
import com.example.cm2601_cw_javafx.model.User;
import java.sql.SQLException;
import java.util.List;

public class SystemUserManager {

    private final DBManager dbManager;

    public SystemUserManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public String registerUser(String username, String password, String confirmPassword, List<Category> selectedCategories) {
        if (!validateUsername(username)) {
            return "Username must be at least 4 characters and only contain letters.";
        }

        try {
            if (DBManager.usernameExists(username)) {
                return "Username already exists!";
            }
        } catch (SQLException e) {
            return "Database error during username check: " + e.getMessage();
        }

        if (!validatePassword(password)) {
            return "Password must be at least 8 characters with letters and numbers.";
        }

        if (!password.equals(confirmPassword)) {
            return "Passwords do not match!";
        }

        if (selectedCategories.size() < 2) {
            return "Please select at least two categories.";
        }

        try {
            int userId = DBManager.insertUser(username, password);
            DBManager.insertUserPreferences(userId, selectedCategories);
            User user = new User(userId, username, password, selectedCategories);
            return "User successfully registered!";

        } catch (SQLException e) {
            return "Registration failed: " + e.getMessage();
        }
    }

    public String authenticateUser(String username, String password) {
        try {


            SystemUser systemUser = DBManager.getUser(username);

            if (systemUser == null) {
                return "Username not found.";
            }

            String storedPassword = systemUser.getPassword();

            if (!password.equals(storedPassword)) {
                return "Invalid password.";
            }

            return "Login successful!";
        } catch (SQLException e) {
            return "Login failed due to database error: " + e.getMessage();
        }
    }


    public SystemUser getUser(String username) {
        try {
            return DBManager.getUser(username);
        } catch (SQLException e) {
            System.out.println("Error retrieving user by username: " + e.getMessage());
            return null;
        }
    }

    public int getUserIdByUsername(String username) {
        try {
            return DBManager.getUserIdByUsername(username);
        } catch (SQLException e) {
            System.out.println("Error retrieving user ID by username: " + e.getMessage());
            return -1;
        }
    }

    public boolean validateUsername(String username) {
        return username.length() >= 4 && username.matches("[A-Za-z]+");
    }

    public boolean validatePassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
    }

    public void likeArticle(int userId, int articleId) {

        DBManager.addInteraction(userId, articleId, "LIKE");
        System.out.println("Article liked successfully!");

    }

    public void unlikeArticle(int userId, int articleId) {

        DBManager.removeInteraction(userId, articleId);
        System.out.println("Article unliked successfully!");

    }

    public boolean hasLikedArticle(int userId, int articleId) {
        return dbManager.hasInteraction(userId,articleId,"LIKE");

    }

    public List<Article> getLikedArticles(int userId) {
        try {
            return dbManager.getArticlesByInteraction(userId, "LIKE");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void skipArticle(int userId, int articleId) {
        DBManager.addInteraction(userId, articleId, "SKIP");
        System.out.println("Article skipped successfully!");

    }

    public void unskipArticle(int userId, int articleId) {
        DBManager.removeInteraction(userId, articleId);
        System.out.println("Article unskipped successfully!");
    }

    public boolean hasSkippedArticle(int userId, int articleId) {
        return dbManager.hasInteraction(userId, articleId, "SKIP");

    }

    public List<Article> getSkippedArticles(int userId) {
        try {
            return dbManager.getArticlesByInteraction(userId, "SKIP");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }





}