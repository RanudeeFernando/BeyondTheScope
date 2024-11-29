package com.example.cm2601_cw_javafx;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public int insertUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            throw new SQLException("Failed to retrieve userID.");
        }
    }

    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

    public Object[] getUserInfo(String username) throws SQLException {
        String sql = "SELECT userID, password, role FROM user WHERE username = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userID = resultSet.getInt("userID");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                return new Object[]{userID, password, role};
            }
        }
        return null;
    }

    public int getUserIdByUsername(String username) throws SQLException {
        String sql = "SELECT userID FROM user WHERE username = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("userID");
            }
        }
        return -1;
    }

    public boolean validateCurrentPassword(String username, String enteredPassword) throws SQLException {
        String sql = "SELECT password FROM user WHERE username = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                return storedPassword.equals(enteredPassword);
            }
        }
        return false;
    }

    public void updatePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE user SET password = ? WHERE userID = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newPassword);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }


    public void insertUserPreferences(int userId, List<Category> categories) throws SQLException {
        String sql = "INSERT INTO user_category (userID, categoryID) VALUES (?, ?)";

        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Category category : categories) {
                // Fetch categoryID based on category name
                int categoryID = getCategoryID(category.name());

                statement.setInt(1, userId);
                statement.setInt(2, categoryID);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error inserting user preferences: " + e.getMessage());
            throw e;
        }
    }

    private int getCategoryID(String categoryName) throws SQLException {
        String sql = "SELECT categoryID FROM Category WHERE categoryName = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoryName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("categoryID");
            } else {
                throw new SQLException("Category not found: " + categoryName);
            }
        }
    }

    public void updateUserPreferences(int userId, List<Category> categories) throws SQLException {
        String deleteSql = "DELETE FROM user_category WHERE userID = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {

            deleteStatement.setInt(1, userId);
            deleteStatement.executeUpdate();
        }

        String insertSql = "INSERT INTO user_category (userID, categoryID) VALUES (?, ?)";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

            for (Category category : categories) {
                int categoryID = getCategoryID(category.name());
                insertStatement.setInt(1, userId);
                insertStatement.setInt(2, categoryID);
                insertStatement.executeUpdate();
            }
        }
    }

    public List<Category> getUserPreferences(int userId) throws SQLException {
        String sql = "SELECT c.categoryName FROM user_category uc " +
                "JOIN Category c ON uc.categoryID = c.categoryID " +
                "WHERE uc.userID = ?";
        List<Category> categories = new ArrayList<>();

        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                categories.add(Category.valueOf(resultSet.getString("categoryName")));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user preferences: " + e.getMessage());
            throw e;
        }
        return categories;
    }


    public void addInteraction(int userId, int articleId, String interactionType) throws SQLException {
        String sql = "INSERT INTO user_article_interaction (userID, articleID, interactionType) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE interactionType = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, articleId);
            statement.setString(3, interactionType);
            statement.setString(4, interactionType); // Update if already exists
            statement.executeUpdate();
        }
    }

    public void removeInteraction(int userId, int articleId) throws SQLException {
        String sql = "DELETE FROM user_article_interaction WHERE userID = ? AND articleID = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, articleId);
            statement.executeUpdate();
        }
    }
    public boolean hasInteraction(int userId, int articleId, String interactionType) throws SQLException {
        String sql = "SELECT COUNT(*) FROM user_article_interaction WHERE userID = ? AND articleID = ? AND interactionType = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, articleId);
            statement.setString(3, interactionType);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) > 0;
        }
    }

    public List<Article> getArticlesByInteraction(int userId, String interactionType) throws SQLException {
        String sql = "SELECT a.*, c.categoryName FROM article a " +
                "INNER JOIN user_article_interaction ui ON a.articleID = ui.articleID " +
                "LEFT JOIN Category c ON a.categoryID = c.categoryID " +
                "WHERE ui.userID = ? AND ui.interactionType = ?";
        List<Article> articles = new ArrayList<>();

        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setString(2, interactionType);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int articleId = resultSet.getInt("articleID");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String author = resultSet.getString("author");
                String source = resultSet.getString("source");
                String url = resultSet.getString("url");
                Timestamp publishedDate = resultSet.getTimestamp("publishedDate");
                String categoryName = resultSet.getString("categoryName");

                Category category = categoryName != null ? Category.valueOf(categoryName) : Category.UNKNOWN;

                // Create an Article object
                Article article = new Article(articleId, title, content, category, author, source, url, publishedDate);
                articles.add(article);
            }
        }
        return articles;
    }

}
