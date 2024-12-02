package com.example.cm2601_cw_javafx.model;

import com.example.cm2601_cw_javafx.db.DBManager;

public class Admin extends SystemUser {

    private final DBManager DBManager = new DBManager();

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
