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

    public void insertUserPreferences(int userId, List<Category> categories) throws SQLException {
        String sql = "INSERT INTO user_category (userID, categoryName) VALUES (?, ?)";

        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Category category : categories) {
                statement.setInt(1, userId);
                statement.setString(2, category.getName());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error inserting user preferences: " + e.getMessage());
            throw e;
        }
    }

    public List<Category> getUserPreferences(int userId) throws SQLException {
        String sql = "SELECT categoryName FROM user_category WHERE userID = ?";
        List<Category> categories = new ArrayList<>();

        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                categories.add(Category.fromString(resultSet.getString("categoryName")));
            }
        }
        return categories;
    }

    public void likeArticle(int userId, int articleId) throws SQLException {
        String sql = "INSERT INTO user_liked_article (userID, articleID) VALUES (?, ?)";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, articleId);
            statement.executeUpdate();
        }
    }

    public void unlikeArticle(int userId, int articleId) throws SQLException {
        String sql = "DELETE FROM user_liked_article WHERE userID = ? AND articleID = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, articleId);
            statement.executeUpdate();
        }
    }


    public boolean hasLikedArticle(int userId, int articleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM user_liked_article WHERE userID = ? AND articleID = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, articleId);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) > 0;
        }
    }

    public List<Article> getLikedArticles(int userId) throws SQLException {
        String sql = "SELECT a.* FROM article a INNER JOIN user_liked_article la ON a.articleID = la.articleID WHERE la.userID = ?";
        List<Article> likedArticles = new ArrayList<>();

        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

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
        }
        return likedArticles;
    }

    public void skipArticle(int userId, int articleId) throws SQLException {
        String sql = "INSERT INTO user_skipped_article (userID, articleID) VALUES (?, ?)";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, articleId);
            statement.executeUpdate();
        }
    }


    public void unskipArticle(int userId, int articleId) throws SQLException {
        String sql = "DELETE FROM user_skipped_article WHERE userID = ? AND articleID = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, articleId);
            statement.executeUpdate();
        }
    }


    public boolean hasSkippedArticle(int userId, int articleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM user_skipped_article WHERE userID = ? AND articleID = ?";
        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, articleId);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) > 0;
        }
    }

    public List<Article> getSkippedArticles(int userId) throws SQLException {
        String sql = "SELECT a.* FROM article a " +
                "INNER JOIN user_skipped_article sa ON a.articleID = sa.articleID " +
                "WHERE sa.userID = ?";
        List<Article> skippedArticles = new ArrayList<>();

        try (Connection connection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

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
                skippedArticles.add(article);
            }
        }
        return skippedArticles;
    }
}
