package com.example.cm2601_cw_javafx;

import java.util.*;

public class User {
    private String userID;
    private String username;
    private String password;
    private List<Category> selectedCategories;
    private List<Article> readingHistory;
    private UserPreference preferences;

    public User(String userID, String username, String password, List<Category> selectedCategories) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.selectedCategories = selectedCategories;
        this.readingHistory = new ArrayList<>();
        this.preferences = new UserPreference(userID);
    }

    // Getter for userID
    public String getUserID() {
        return userID;
    }

    // Setter for userID
    public void setUserID(String userID) {
        this.userID = userID;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    public String getSelectedCategoriesAsString() {
        return String.join(",", selectedCategories.stream().map(Category::name).toList());
    }



}
