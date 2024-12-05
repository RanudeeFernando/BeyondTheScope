package com.example.cm2601_cw_javafx.service;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Category;
import com.example.cm2601_cw_javafx.model.SystemUser;
import com.example.cm2601_cw_javafx.model.User;
import java.sql.SQLException;
import java.util.List;

public class SystemUserManager {

    public String registerUser(String username, String password, String confirmPassword, List<Category> selectedCategories) {
        if (!validateUsername(username)) {
            return "Username must be at least 4 characters and only contain letters.";
        }

        try {
            if (DBManager.checkUsernameExistsQuery(username)) {
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
            int userId = DBManager.insertUserQuery(username, password);
            DBManager.insertUserCategoriesQuery(userId, selectedCategories);

            User user = new User(userId, username, password, selectedCategories);

            return "User successfully registered as: " + user.getUsername();

        } catch (SQLException e) {
            return "Registration failed: " + e.getMessage();
        }
    }

    public String authenticateUser(String username, String password) {
        SystemUser systemUser = DBManager.getUserQuery(username);

        if (systemUser == null) {
            return "Username not found.";
        }

        String storedPassword = systemUser.getPassword();

        if (!password.equals(storedPassword)) {
            return "Invalid password.";
        }

        return "Login successful!";
    }

    public boolean validateUsername(String username) {
        return username.length() >= 4 && username.matches("[A-Za-z]+");
    }

    public boolean validatePassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
    }






}