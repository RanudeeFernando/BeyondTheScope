package com.example.cm2601_cw_javafx;

import java.sql.*;
import java.util.ArrayList;
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

    public int getUserIdByUsername(String username) {
        try (Connection connection = MySQLConnection.connectToDatabase()) {
            String query = "SELECT userID FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("userID"); // User found, return userID
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }


    // Method to check if the user has liked the article
    public boolean hasLikedArticle(int userId, int articleId) {
        String sql = "SELECT COUNT(*) FROM user_liked_article WHERE userID = ? AND articleID = ?";
        try (Connection dbConnection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, articleId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void addLikedArticle(int userId, int articleId) {
        String sql = "INSERT INTO user_liked_article (userID, articleID) VALUES (?, ?)";
        try (Connection dbConnection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, articleId);
            statement.executeUpdate();
            System.out.println("Article liked successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeLikedArticle(int userId, int articleId) {
        String sql = "DELETE FROM user_liked_article WHERE userID = ? AND articleID = ?";
        try (Connection dbConnection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, articleId);
            statement.executeUpdate();
            System.out.println("Article unliked successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to get liked articles for the user
    public List<Article> getLikedArticles(int userId) {
        List<Article> likedArticles = new ArrayList<>();
        String sql = "SELECT a.* FROM article a INNER JOIN user_liked_article la ON a.articleID = la.articleID WHERE la.userID = ?";

        try (Connection dbConnection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int articleId = resultSet.getInt("articleID");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String author = resultSet.getString("author");
                String source = resultSet.getString("source");
                String url = resultSet.getString("url");
                Timestamp publishedDate = resultSet.getTimestamp("publishedDate");
                String categoryName = resultSet.getString("category");

                Category category = Category.fromString(categoryName);

                Article article = new Article(articleId, title, content, category, author, source, url, publishedDate);
                likedArticles.add(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return likedArticles;
    }



}
