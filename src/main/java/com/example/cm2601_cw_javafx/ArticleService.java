package com.example.cm2601_cw_javafx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ArticleService {

    // Method to save an article to the database
    // Method to save an article to the database
    public void saveArticle(Article article) {
        String sql = "INSERT INTO article (title, source, author, content) VALUES (?, ?, ?, ?)";
        try (Connection dbConnection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql)) {

            statement.setString(1, article.getTitle());
            statement.setString(2, article.getSource());
            statement.setString(3, article.getAuthor());
            statement.setString(4, article.getContent());
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all articles from the database
    public List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM article";

        try (Connection dbConnection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String source = resultSet.getString("source");
                String author = resultSet.getString("author");
                String content = resultSet.getString("content");
                String url = resultSet.getString("url");
                Timestamp publishedDate = resultSet.getTimestamp("publishedDate");


                Article article = new Article(null, title, content, null, author, source, url, publishedDate);

                articles.add(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }

    // Since you don't have 'url', we can remove the duplicate check or adjust it
    // For example, check duplicates based on title and source
    public boolean isDuplicateArticle(String title, String source) {
        String sql = "SELECT COUNT(*) FROM article WHERE title = ? AND source = ?";
        try (Connection dbConnection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql)) {

            statement.setString(1, title);
            statement.setString(2, source);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
