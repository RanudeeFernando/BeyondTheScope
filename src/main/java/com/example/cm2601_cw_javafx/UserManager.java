package com.example.cm2601_cw_javafx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserManager {

    public String registerUser(String username, String password, String confirmPassword, List<Category> selectedCategories) {
        if (!validateUsername(username)) {
            return "Username must be at least 4 characters and only contain letters.";
        }
        if (usernameExists(username)) {
            return "Username already exists!";
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

        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement userStatement = connection.prepareStatement("INSERT INTO user (username, password) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {

            userStatement.setString(1, username);
            userStatement.setString(2, password);
            userStatement.executeUpdate();

            ResultSet generatedKeys = userStatement.getGeneratedKeys();
            int userId = -1;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            } else {
                return "Failed to retrieve generated user ID.";
            }

            // Insert into 'user_category' table for each selected category
            try (PreparedStatement categoryStatement = connection.prepareStatement(
                    "INSERT INTO user_category (userID, categoryName) VALUES (?, ?)")) {
                for (Category category : selectedCategories) {
                    categoryStatement.setInt(1, userId);
                    categoryStatement.setString(2, category.getName()); // Use the category name directly
                    categoryStatement.executeUpdate();
                }
            }

            return "User successfully registered!";
        } catch (SQLException e) {
            return "Registration failed. Please try again: " + e.getMessage();
        }
    }

    private boolean validateUsername(String username) {
        return username.length() >= 4 && username.matches("[A-Za-z]+");
    }

    private boolean validatePassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
    }

    private boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement checkUsernameStatement = connection.prepareStatement(sql)) {
            checkUsernameStatement.setString(1, username);
            ResultSet resultSet = checkUsernameStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String loginUser(String username, String password) {
        String sql = "SELECT password FROM user WHERE username = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement loginStatement = connection.prepareStatement(sql)) {

            loginStatement.setString(1, username);
            ResultSet resultSet = loginStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (storedPassword.equals(password)) {
                    return "Login successful!";
                } else {
                    return "Invalid password.";
                }
            } else {
                return "Username not found.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Login failed. Please try again: " + e.getMessage();
        }
    }

}
