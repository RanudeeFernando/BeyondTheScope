package com.example.cm2601_cw_javafx;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleService {

    // Method to retrieve all articles from the database
//    public List<Article> getAllArticles() {
//        List<Article> articles = new ArrayList<>();
//        String sql = "SELECT * FROM article";
//
//        try (Connection dbConnection = MySQLConnection.connectToDatabase();
//             PreparedStatement statement = dbConnection.prepareStatement(sql);
//             ResultSet resultSet = statement.executeQuery()) {
//
//            while (resultSet.next()) {
//                int articleId = resultSet.getInt("articleID");
//                String title = resultSet.getString("title");
//                String source = resultSet.getString("source");
//                String author = resultSet.getString("author");
//                String content = resultSet.getString("content");
//                String url = resultSet.getString("url");
//                Timestamp publishedDate = resultSet.getTimestamp("publishedDate");
//                String categoryName = resultSet.getString("category");
//
//                Category category = Category.fromString(categoryName);
//
//
//                Article article = new Article(articleId, title, content, category, author, source, url, publishedDate);
//
//                articles.add(article);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return articles;
//    }

    public List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT a.*, c.categoryName FROM article a " +
                "LEFT JOIN Category c ON a.categoryID = c.categoryID";

        try (Connection dbConnection = MySQLConnection.connectToDatabase();
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

        try (Connection dbConnection = MySQLConnection.connectToDatabase();
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

                UserDAO userDAO = new UserDAO();
                SystemUserManager systemUserManager = new SystemUserManager(userDAO);

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

        try (Connection dbConnection = MySQLConnection.connectToDatabase();
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

        try (Connection connection = MySQLConnection.connectToDatabase();
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

        try (Connection connection = MySQLConnection.connectToDatabase();
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

    void updateArticleCategoryInDatabase(Article article) {
        String fetchCategoryIDSql = "SELECT categoryID FROM category WHERE categoryName = ?";
        String updateArticleSql = "UPDATE article SET categoryID = ? WHERE articleID = ?";

        try (Connection dbConnection = MySQLConnection.connectToDatabase();
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





}
