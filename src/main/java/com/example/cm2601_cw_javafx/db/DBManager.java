package com.example.cm2601_cw_javafx.db;

import com.example.cm2601_cw_javafx.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBManager {

    private static final String URL = "jdbc:mysql://localhost:3306/news_recommendation_system_db";
    private static final String USER = "root";
    private static final String PASSWORD = "ranu2004";

    private static final ExecutorService executor = Executors.newFixedThreadPool(2);


    public static Connection connectToDatabase() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return connection;

        } catch (SQLException e) {
            System.out.println("There was an error when connecting to MySQL database.");
            return null;
        }
    }

    public static void addInteractionQuery(int userId, int articleId, String interactionType) {
        executor.submit(() -> {
            String sql = "INSERT INTO user_article_interaction (userID, articleID, interactionType) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE interactionType = ?";
            try (Connection connection = connectToDatabase();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, userId);
                statement.setInt(2, articleId);
                statement.setString(3, interactionType);
                statement.setString(4, interactionType); // Update if already exists
                statement.executeUpdate();
            } catch (SQLException e) {
                // Handle the SQLException
                System.err.println("Error while adding interaction: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public static void removeInteractionQuery(int userId, int articleId){
        executor.submit(() -> {
            String sql = "DELETE FROM user_article_interaction WHERE userID = ? AND articleID = ?";
            try (Connection connection = connectToDatabase();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, userId);
                statement.setInt(2, articleId);
                statement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println("Error while removing interaction: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public static void updateArticleCategoryQuery(Article article) {
        executor.submit(() -> {
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
        });
    }

    public static void insertViewedArticleQuery(int userId, int articleId) {
        executor.submit(() -> {
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
        });
    }

    public static void insertArticleQuery(Article article) {
        executor.submit(() -> {
            String sql = "INSERT INTO article (title, source, author, content, url, publishedDate, categoryID) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (Connection dbConnection = connectToDatabase();
                 PreparedStatement statement = dbConnection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, article.getTitle());
                statement.setString(2, article.getSource());
                statement.setString(3, article.getAuthor());
                statement.setString(4, article.getContent());
                statement.setString(5, article.getUrl());
                statement.setTimestamp(6, article.getPublishedDate());

                int categoryID = getCategoryIDQuery(article.getCategory().name());
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
        });
    }



    public static int insertUserQuery(String username, String password) {

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
                else return -1;
            }
            catch (SQLException e) {
                System.out.println("Error inserting user preferences: " + e.getMessage());
                return -1;
            }

    }

    public static boolean checkUsernameExistsQuery(String username) throws SQLException {
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

    public static SystemUser getUserQuery(String username){
        String sql = "SELECT userID, password, role FROM user WHERE username = ?";
        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userID = resultSet.getInt("userID");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                // Create the appropriate object based on the role
                if ("ADMIN".equalsIgnoreCase(role)) {
                    return new Admin(userID, username, password); // Assuming Admin extends SystemUser
                } else if ("USER".equalsIgnoreCase(role)) {
                    return new User(userID, username, password); // Assuming User extends SystemUser
                } else {
                    throw new IllegalArgumentException("Unknown role: " + role);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserting user preferences: " + e.getMessage());
        }
        return null;
    }


    public static boolean equalsCurrentPasswordQuery(String username, String enteredPassword) throws SQLException {
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

    public static void updatePasswordQuery(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE user SET password = ? WHERE userID = ?";
        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newPassword);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }


    public static void insertUserCategoriesQuery(int userId, List<Category> categories) throws SQLException {
        String sql = "INSERT INTO user_category (userID, categoryID) VALUES (?, ?)";

        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Category category : categories) {
                int categoryID = getCategoryIDQuery(category.name());

                statement.setInt(1, userId);
                statement.setInt(2, categoryID);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error inserting user preferences: " + e.getMessage());
            throw e;
        }
    }


    public static void updateUserCategoriesQuery(int userId, List<Category> categories) throws SQLException {
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
                int categoryID = getCategoryIDQuery(category.name());
                insertStatement.setInt(1, userId);
                insertStatement.setInt(2, categoryID);
                insertStatement.executeUpdate();
            }
        }
    }

    public static List<Category> getUserCategoriesQuery(int userId) throws SQLException {
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


    public static boolean findInteractionQuery(int userId, int articleId, String interactionType) {
        String sql = "SELECT COUNT(*) FROM user_article_interaction WHERE userID = ? AND articleID = ? AND interactionType = ?";
        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, articleId);
            statement.setString(3, interactionType);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static List<Article> getArticlesByInteractionQuery(int userId, String interactionType) throws SQLException{
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

    public static List<Article> getAllArticlesQuery() {
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
                String categoryName = resultSet.getString("categoryName");

                Category category = categoryName != null ? Category.valueOf(categoryName) : Category.UNKNOWN;

                Article article = new Article(articleId, title, content, category, author, source, url, publishedDate);

                articles.add(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }


    public static boolean deleteArticleByIDQuery(int articleID) {
        String sql = "DELETE FROM article WHERE articleID = ?";

        try (Connection dbConnection = connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql)) {

            statement.setInt(1, articleID);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Article> getArticlesWithUnknownCategoryQuery() {
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


    public static Article getArticleByIDQuery(int articleId) {
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

    public static String getArticleNameByIdQuery(String itemId) {
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


    // Method to retrieve all viewed articles by a user
    public static List<ViewedArticle> getViewedArticlesQuery(int userId) {
        List<ViewedArticle> viewedArticles = new ArrayList<>();
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
                Timestamp viewedAt = resultSet.getTimestamp("viewedAt");

                ViewedArticle viewedArticle = new ViewedArticle(articleId, title, viewedAt);
                viewedArticles.add(viewedArticle);
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving viewed articles: " + e.getMessage());
            e.printStackTrace();
        }

        return viewedArticles;
    }


    private static int getCategoryIDQuery(String categoryName) throws SQLException {
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


    public static boolean isDuplicateArticleQuery(String url) {
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

    public static void shutdownExecutor() {
        executor.shutdown();
        System.out.println("Executor service shut down.");
    }

    // ---------------------

//    public Article getArticleById(int articleId) {
//        String sql = "SELECT a.*, c.categoryName FROM article a " +
//                "LEFT JOIN Category c ON a.categoryID = c.categoryID " +
//                "WHERE a.articleID = ?";
//        Article article = null;
//
//        try (Connection connection = connectToDatabase();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//
//            statement.setInt(1, articleId);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    String title = resultSet.getString("title");
//                    String content = resultSet.getString("content");
//                    String author = resultSet.getString("author");
//                    String source = resultSet.getString("source");
//                    String url = resultSet.getString("url");
//                    Timestamp publishedDate = resultSet.getTimestamp("publishedDate");
//                    String categoryName = resultSet.getString("categoryName");
//
//                    Category category;
//                    try {
//                        category = categoryName != null ? Category.valueOf(categoryName) : Category.UNKNOWN;
//                    } catch (IllegalArgumentException e) {
//                        category = Category.UNKNOWN;
//                    }
//
//                    article = new Article(articleId, title, content, category, author, source, url, publishedDate);
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("An error occurred while fetching the article: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return article;
//    }
//
//    public static int getUserIdByUsername(String username) throws SQLException {
//        String sql = "SELECT userID FROM user WHERE username = ?";
//        try (Connection connection = connectToDatabase();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//
//            statement.setString(1, username);
//            ResultSet resultSet = statement.executeQuery();
//
//            if (resultSet.next()) {
//                return resultSet.getInt("userID");
//            }
//        }
//        return -1;
//    }

}





