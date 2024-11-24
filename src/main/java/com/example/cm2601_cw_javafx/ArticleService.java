package com.example.cm2601_cw_javafx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ArticleService {

    // Method to retrieve all articles from the database
    public List<Article> getAllArticles() {
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
                String categoryName = resultSet.getString("category");

                Category category = Category.fromString(categoryName);


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



}
