package com.example.cm2601_cw_javafx;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class UserDatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:users.db"; // SQLite database URL

    public UserDatabaseManager() {
        createTables();
    }

    private void createTables() {
        String userTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "userID TEXT PRIMARY KEY, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL)";
        String categoryTableSQL = "CREATE TABLE IF NOT EXISTS categories (" +
                "userID TEXT, " +
                "category TEXT, " +
                "FOREIGN KEY(userID) REFERENCES users(userID))";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement()) {
            stmt.execute(userTableSQL);
            stmt.execute(categoryTableSQL);
            System.out.println("CONNECTED TO SQLITE");
        } catch (SQLException e) {
            System.out.println("IM TRYINGGGG");
        }
    }

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

        String userID = UUID.randomUUID().toString();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmtUser = conn.prepareStatement("INSERT INTO users (userID, username, password) VALUES (?, ?, ?)");
             PreparedStatement pstmtCategory = conn.prepareStatement("INSERT INTO categories (userID, category) VALUES (?, ?)")) {

            pstmtUser.setString(1, userID);
            pstmtUser.setString(2, username);
            pstmtUser.setString(3, password);
            pstmtUser.executeUpdate();

            for (Category category : selectedCategories) {
                pstmtCategory.setString(1, userID);
                pstmtCategory.setString(2, category.toString());
                pstmtCategory.executeUpdate();
            }

            return "User successfully registered!";
        } catch (SQLException e) {
            return "Registration failed. Please try again.";
        }
    }

    private boolean validateUsername(String username) {
        return username.length() >= 8 && username.matches("[A-Za-z]+");
    }

    private boolean validatePassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
    }

    private boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

