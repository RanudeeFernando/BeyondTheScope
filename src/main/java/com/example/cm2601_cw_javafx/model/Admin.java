package com.example.cm2601_cw_javafx.model;

import com.example.cm2601_cw_javafx.db.DBManager;

public class Admin extends SystemUser {
    // private final ArticleService articleService;
    private final DBManager DBManager = new DBManager();

    public Admin(int userID, String username, String password) {
        super(userID, username, password);
        //this.articleService = new ArticleService();
    }

    public Admin(int userID, String username){
        super(userID, username);
        //this.articleService = new ArticleService();

    }

    @Override
    public String getRole() {
        return "Admin";
    }

    public boolean deleteArticleByID(String articleID) {
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



}
