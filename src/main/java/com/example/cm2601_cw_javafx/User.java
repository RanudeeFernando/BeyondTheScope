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


//    public boolean validatePassword(String password) {
//        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
//    }
//
//    // Method to generate user ID
//    public static String generateUserID() {
//        return UUID.randomUUID().toString(); // Generates a unique ID
//    }
//
//
//
//
//
//    public String register(String username, String password, String confirmPassword, List<Category> selectedCategories) {
//        UserStorage userStorage = new UserStorage();
//        if (userStorage.usernameExists(username)) {
//            return "Username already exists!";
//        }
//        if (!validatePassword(password)) {
//            return "Password must be at least 8 characters with letters and numbers.";
//        }
//        if (!password.equals(confirmPassword)) {
//            return "Passwords do not match!";
//        }
//        if (selectedCategories.size() < 2) {
//            return "Please select at least two categories.";
//        }
//
//        String userID = generateUserID();
//        User user = new User(userID, username, password, selectedCategories);
//        userStorage.storeUser(user);
//
//        return "User successfully registered!";
//    }



}
