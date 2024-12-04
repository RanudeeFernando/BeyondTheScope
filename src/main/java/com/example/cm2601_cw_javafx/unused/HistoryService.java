//package com.example.cm2601_cw_javafx;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class HistoryService {
//    private final Connection connection;
//
//    // Constructor to accept and set up a database connection
//    public HistoryService(Connection connection) {
//        this.connection = connection;
//    }
//
//    // Method to add a viewed article
//    public void addViewedArticle(int userId, int articleId) {
//        String insertSQL = "INSERT INTO user_viewed_article (userID, articleID, viewedAt) VALUES (?, ?, ?)";
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
//            preparedStatement.setInt(1, userId);
//            preparedStatement.setInt(2, articleId);
//            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
//
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println("Error while inserting viewed article: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    // Method to retrieve all viewed articles by a user
//    public List<ViewedArticle> getViewedArticles(int userId) {
//        List<ViewedArticle> viewedArticles = new ArrayList<>();
//        String querySQL = "SELECT a.articleID, a.title, a.content, a.author, a.source, a.url, a.publishedDate, u.viewedAt " +
//                "FROM user_viewed_article u " +
//                "JOIN article a ON u.articleID = a.articleID " +
//                "WHERE u.userID = ? " +
//                "ORDER BY u.viewedAt DESC";
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(querySQL)) {
//            preparedStatement.setInt(1, userId);
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                int articleId = resultSet.getInt("articleID");
//                String title = resultSet.getString("title");
//                String content = resultSet.getString("content");
//                String author = resultSet.getString("author");
//                String source = resultSet.getString("source");
//                String url = resultSet.getString("url");
//                Timestamp publishedDate = resultSet.getTimestamp("publishedDate");
//                Timestamp viewedAt = resultSet.getTimestamp("viewedAt");
//
//                // Create the Article object
//                // Article article = new Article(articleId, title, content, null, author, source, url, publishedDate);
//                // Wrap it in a ViewedArticle
//                ViewedArticle userViewedArticle = new ViewedArticle(articleId, title, content, null, author, source, url, publishedDate, viewedAt);
//                viewedArticles.add(userViewedArticle);
//            }
//        } catch (SQLException e) {
//            System.out.println("Error while retrieving viewed articles: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return viewedArticles;
//    }
//}
