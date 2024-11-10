package com.example.cm2601_cw_javafx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/news_recommendation_system_db";

    public String registerUser(String username, String password, String confirmPassword, List<Category> selectedCategories) {
        if (!validateUsername(username)) {
            return "Username must be at least 8 characters and only contain letters.";
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

        try (Connection conn = MySQLConnection.connectToDatabase();
             PreparedStatement userStatement = conn.prepareStatement("INSERT INTO user (username, password) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {

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

            return "User successfully registered!";
        } catch (SQLException e) {
            return "Registration failed. Please try again: " + e.getMessage();
        }
    }

    private boolean validateUsername(String username) {
        return username.length() >= 8 && username.matches("[A-Za-z]+");
    }

    private boolean validatePassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
    }

    private boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
        try (Connection conn = MySQLConnection.connectToDatabase();
             PreparedStatement checkUsernameStatement = conn.prepareStatement(sql)) {
            checkUsernameStatement.setString(1, username);
            ResultSet rs = checkUsernameStatement.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
