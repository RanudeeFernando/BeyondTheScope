package com.example.cm2601_cw_javafx.db;

import com.example.cm2601_cw_javafx.model.Category;
import com.example.cm2601_cw_javafx.service.SystemUserManager;
import com.example.cm2601_cw_javafx.model.UserViewedArticle;
import com.example.cm2601_cw_javafx.model.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDBManager {

    private static final String URL = "jdbc:mysql://localhost:3306/news_recommendation_system_db";

    // private static final String URL = "jdbc:mysql://localhost:3306/news_rec_db";
    private static final String USER = "root";
    private static final String PASSWORD = "ranu2004";


    public static Connection connectToDatabase() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return connection;

        } catch (SQLException e) {
            System.out.println("There was an error when connecting to MySQL database.");
            return null;
        }
    }

    public int insertUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        try (Connection connection = connectToDatabase();
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
        try (Connection connection = connectToDatabase();
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
        try (Connection connection = connectToDatabase();
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
        try (Connection connection = connectToDatabase();
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
        try (Connection connection = connectToDatabase();
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
        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newPassword);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }


    public void insertUserPreferences(int userId, List<Category> categories) throws SQLException {
        String sql = "INSERT INTO user_category (userID, categoryID) VALUES (?, ?)";

        try (Connection connection = connectToDatabase();
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


    public void updateUserPreferences(int userId, List<Category> categories) throws SQLException {
        String deleteSql = "DELETE FROM user_category WHERE userID = ?";
        try (Connection connection = connectToDatabase();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {

            deleteStatement.setInt(1, userId);
            deleteStatement.executeUpdate();
        }

        String insertSql = "INSERT INTO user_category (userID, categoryID) VALUES (?, ?)";
        try (Connection connection = connectToDatabase();
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

        try (Connection connection = connectToDatabase();
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
        try (Connection connection = connectToDatabase();
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
        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, articleId);
            statement.executeUpdate();
        }
    }
    public boolean hasInteraction(int userId, int articleId, String interactionType) throws SQLException {
        String sql = "SELECT COUNT(*) FROM user_article_interaction WHERE userID = ? AND articleID = ? AND interactionType = ?";
        try (Connection connection = connectToDatabase();
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

        try (Connection connection = connectToDatabase();
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

                Article article = new Article(articleId, title, content, category, author, source, url, publishedDate);
                articles.add(article);
            }
        }
        return articles;
    }

    // -----------------ARTICLE SERVICE----------------------------------------------

    public List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT a.*, c.categoryName FROM article a " +
                "LEFT JOIN Category c ON a.categoryID = c.categoryID";

        try (Connection dbConnection = connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int articleId = resultSet.getInt("articleID");
                String title = resultSet.getString("title");
                String source = resultSet.getString("source");
                String author = resultSet.getString("author");
                String content = resultSet.getString("content");
                String url = resultSet.getString("url");
                Timestamp publishedDate = resultSet.getTimestamp("publishedDate");
                String categoryName = resultSet.getString("categoryName"); // From the JOIN with Category table

                Category category = categoryName != null ? Category.valueOf(categoryName) : Category.UNKNOWN;

                Article article = new Article(articleId, title, content, category, author, source, url, publishedDate);

                articles.add(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }


    // Method to retrieve all articles from the database, excluding skipped articles
    public List<Article> getAllArticles(int userId) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM article";

        try (Connection dbConnection = connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int articleId = resultSet.getInt("articleID");
                String title = resultSet.getString("title");
                String source = resultSet.getString("source");
                String author = resultSet.getString("author");
                String content = resultSet.getString("content");
                String url = resultSet.getString("url");
                Timestamp publishedDate = resultSet.getTimestamp("publishedDate");

                // RegularUserManager regularUserManager = new RegularUserManager();

                UserDBManager userDBManager = new UserDBManager();
                SystemUserManager systemUserManager = new SystemUserManager(userDBManager);

                // Check if the user has skipped this article
                if (!systemUserManager.hasSkippedArticle(userId, articleId)) {
                    Article article = new Article(articleId, title, content, null, author, source, url, publishedDate);
                    articles.add(article);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }

    public boolean deleteArticleByID(int articleID) {
        String sql = "DELETE FROM article WHERE articleID = ?";

        try (Connection dbConnection = connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql)) {

            statement.setInt(1, articleID);

            int rowsAffected = statement.executeUpdate(); // Execute the delete operation
            return rowsAffected > 0; // Return true if at least one row was deleted

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getCategoryName(int categoryID) {
        String sql = "SELECT categoryName FROM Category WHERE categoryID = ?";
        String categoryName = "UNKNOWN"; // Default value in case the category is not found

        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                categoryName = resultSet.getString("categoryName");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving category name: " + e.getMessage());
        }

        return categoryName;
    }

    public List<Article> getArticlesWithUnknownCategory() {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT a.articleID, a.title, a.content, a.author, a.source, a.url, "
                + "a.publishedDate, c.categoryName "
                + "FROM article a "
                + "JOIN category c ON a.categoryID = c.categoryID "
                + "WHERE c.categoryName = 'UNKNOWN'";

        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int articleId = resultSet.getInt("articleID");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String author = resultSet.getString("author");
                String source = resultSet.getString("source");
                String url = resultSet.getString("url");
                Timestamp publishedDate = resultSet.getTimestamp("publishedDate");
                String categoryName = resultSet.getString("categoryName"); // Retrieved from Category table

                Category category = Category.UNKNOWN;

                // Create and add the Article object
                Article article = new Article(articleId, title, content, category, author, source, url, publishedDate);
                articles.add(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }

    public void updateArticleCategoryInDatabase(Article article) {
        String fetchCategoryIDSql = "SELECT categoryID FROM category WHERE categoryName = ?";
        String updateArticleSql = "UPDATE article SET categoryID = ? WHERE articleID = ?";

        try (Connection dbConnection = connectToDatabase();
             PreparedStatement fetchCategoryIDStmt = dbConnection.prepareStatement(fetchCategoryIDSql);
             PreparedStatement updateArticleStmt = dbConnection.prepareStatement(updateArticleSql)) {

            // Fetch the categoryID using the predicted category name
            fetchCategoryIDStmt.setString(1, article.getCategory().name());
            ResultSet resultSet = fetchCategoryIDStmt.executeQuery();

            if (resultSet.next()) {
                int categoryID = resultSet.getInt("categoryID");

                // Update the article with the new categoryID
                updateArticleStmt.setInt(1, categoryID);
                updateArticleStmt.setInt(2, article.getArticleID());
                int rowsUpdated = updateArticleStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Article category updated successfully for: " + article.getTitle());
                } else {
                    System.out.println("No article found with the given ID.");
                }
            } else {
                System.out.println("Category not found in the database for: " + article.getTitle());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Article getArticleByID(int articleId) {
        String sql = "SELECT a.*, c.categoryName FROM article a " +
                "LEFT JOIN category c ON a.categoryID = c.categoryID " +
                "WHERE a.articleID = ?";

        try (Connection dbConnection = connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql)) {

            statement.setInt(1, articleId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String source = resultSet.getString("source");
                String author = resultSet.getString("author");
                String content = resultSet.getString("content");
                String url = resultSet.getString("url");
                Timestamp publishedDate = resultSet.getTimestamp("publishedDate");
                String categoryName = resultSet.getString("categoryName"); // From the JOIN with Category table

                Category category = categoryName != null ? Category.valueOf(categoryName) : Category.UNKNOWN;

                return new Article(articleId, title, content, category, author, source, url, publishedDate);
            } else {
                System.out.println("No article found with ID: " + articleId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getArticleNameById(String itemId) {
        String query = "SELECT title FROM article WHERE articleID = ?";
        String articleName = "[No Title]";

        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, itemId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                articleName = resultSet.getString("title");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return articleName;
    }


    // -------------HISTORY SERVICE-------------------

    public void addViewedArticle(int userId, int articleId) {
        String insertSQL = "INSERT INTO user_viewed_article (userID, articleID, viewedAt) VALUES (?, ?, ?)";

        try (Connection dbConnection = connectToDatabase();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, articleId);
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while inserting viewed article: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to retrieve all viewed articles by a user
    public List<UserViewedArticle> getViewedArticles(int userId) {
        List<UserViewedArticle> viewedArticles = new ArrayList<>();
        String querySQL = "SELECT a.articleID, a.title, a.content, a.author, a.source, a.url, a.publishedDate, u.viewedAt " +
                "FROM user_viewed_article u " +
                "JOIN article a ON u.articleID = a.articleID " +
                "WHERE u.userID = ? " +
                "ORDER BY u.viewedAt DESC";

        try (Connection dbConnection = connectToDatabase();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(querySQL)) {
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int articleId = resultSet.getInt("articleID");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String author = resultSet.getString("author");
                String source = resultSet.getString("source");
                String url = resultSet.getString("url");
                Timestamp publishedDate = resultSet.getTimestamp("publishedDate");
                Timestamp viewedAt = resultSet.getTimestamp("viewedAt");

                // Create the Article object
                // Article article = new Article(articleId, title, content, null, author, source, url, publishedDate);
                // Wrap it in a UserViewedArticle
                UserViewedArticle userViewedArticle = new UserViewedArticle(articleId, title, content, null, author, source, url, publishedDate, viewedAt);
                viewedArticles.add(userViewedArticle);
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving viewed articles: " + e.getMessage());
            e.printStackTrace();
        }

        return viewedArticles;
    }

    // -----------------------------FETCH ARTICLES ------------------------------------------

    public static void addArticleToDatabase(Article article) {
        String sql = "INSERT INTO article (title, source, author, content, url, publishedDate, categoryID) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection dbConnection = connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, article.getTitle());
            statement.setString(2, article.getSource());
            statement.setString(3, article.getAuthor());
            statement.setString(4, article.getContent());
            statement.setString(5, article.getUrl());
            statement.setTimestamp(6, article.getPublishedDate());

            int categoryID = getCategoryID(article.getCategory().name());
            statement.setInt(7, categoryID);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedArticleID = generatedKeys.getInt(1);
                    article.setArticleID(generatedArticleID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getCategoryID(String categoryName) throws SQLException {
        String sql = "SELECT categoryID FROM Category WHERE categoryName = ?";
        try (Connection connection = connectToDatabase();
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


    public static boolean isDuplicateArticle(String url) {
        String sql = "SELECT COUNT(*) FROM article WHERE url = ?";
        try (Connection dbConnection = connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql)) {

            statement.setString(1, url);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
