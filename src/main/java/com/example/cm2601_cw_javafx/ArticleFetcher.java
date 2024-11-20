package com.example.cm2601_cw_javafx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

public class ArticleFetcher {
    private static final String API_KEY = "829613513f4a4c9794a7ecfc44a91b0c";
    private static final String API_URL = "https://newsapi.org/v2/top-headlines?language=en&pageSize=10&apiKey=" + API_KEY;

//    public static List<String> fetchArticles() {
//        List<String> articleTitles = new ArrayList<>();
//        try {
//            URL url = new URL(API_URL);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String inputLine;
//                StringBuilder response = new StringBuilder();
//
//                while ((inputLine = reader.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                reader.close();
//
//                JSONObject jsonResponse = new JSONObject(response.toString());
//                JSONArray articles = jsonResponse.getJSONArray("articles");
//
//                for (int i = 0; i < articles.length(); i++) {
//                    JSONObject article = articles.getJSONObject(i);
//                    String title = article.optString("title", null);
//                    String sourceName = article.getJSONObject("source").optString("name", null);
//                    String author = article.optString("author", null);
//                    String content = article.optString("content", null);
//                    String urlField = article.optString("url", null);
//                    String publishedDate = article.optString("publishedAt", null);
//
//                    // Check if title and source are present before proceeding
//                    if (isValidArticle(title, sourceName, author, content, urlField) && !isDuplicateArticle(urlField)) {
//                        articleTitles.add(title);
//                        addArticleToDatabase(title, sourceName, author, content, urlField, publishedDate);
//                    }
//                }
//            } else {
//                System.out.println("Failed to fetch articles. Response code: " + responseCode);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return articleTitles;
//    }

    // Fetch articles from the API
    public static List<Article> fetchArticles() {
        List<Article> articles = new ArrayList<>();
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray articlesJsonArray = jsonResponse.getJSONArray("articles");

                for (int i = 0; i < articlesJsonArray.length(); i++) {
                    JSONObject articleJson = articlesJsonArray.getJSONObject(i);
                    String title = articleJson.optString("title", null);
                    String sourceName = articleJson.getJSONObject("source").optString("name", null);
                    String author = articleJson.optString("author", null);
                    String content = articleJson.optString("content", null);
                    String urlField = articleJson.optString("url", null);
                    String publishedDate = articleJson.optString("publishedAt", null);

                    // Check if the article is valid and not a duplicate
                    if (isValidArticle(title, sourceName, author, content, urlField) && !isDuplicateArticle(urlField)) {
                        Timestamp publishedDateTimestamp = parsePublishedDate(publishedDate);

                        // Create an Article object
                        Article article = new Article(0, title, content, null, author, sourceName, urlField, publishedDateTimestamp);
                        articles.add(article);

                        // Add the article to the database
                        addArticleToDatabase(article);
                    }
                }
            } else {
                System.out.println("Failed to fetch articles. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }


    private static boolean isValidArticle(String title, String sourceName, String author, String content, String url) {
        return title != null && !title.trim().isEmpty() && !title.equalsIgnoreCase("[Removed]") &&
                sourceName != null && !sourceName.trim().isEmpty() && !sourceName.equalsIgnoreCase("[Removed]") &&
                url != null && !url.trim().isEmpty() && !url.equalsIgnoreCase("[Removed]") &&
                author != null && !author.trim().isEmpty() && !author.trim().equalsIgnoreCase("[Removed]") &&
                content != null && !content.trim().isEmpty() && !content.equalsIgnoreCase("[Removed]");
    }


//    private static void addArticleToDatabase(String title, String source, String author, String content, String url, String publishedDate) {
//        String sql = "INSERT INTO article (title, source, author, content, url, publishedDate) VALUES (?, ?, ?, ?, ?, ?)";
//        try (Connection dbConnection = MySQLConnection.connectToDatabase();
//             PreparedStatement statement = dbConnection.prepareStatement(sql)) {
//
//            statement.setString(1, title);
//            statement.setString(2, source);
//            statement.setString(3, author);
//            statement.setString(4, content);
//            statement.setString(5, url);
//            statement.setTimestamp(6, parsePublishedDate(publishedDate));
//            statement.executeUpdate();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    // Insert the article into the database
    private static void addArticleToDatabase(Article article) {
        String sql = "INSERT INTO article (title, source, author, content, url, publishedDate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection dbConnection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // Set the parameters from the Article object
            statement.setString(1, article.getTitle());
            statement.setString(2, article.getSource());
            statement.setString(3, article.getAuthor());
            statement.setString(4, article.getContent());
            statement.setString(5, article.getUrl());
            statement.setTimestamp(6, article.getPublishedDate());

            // Execute the insert
            int rowsUpdated = statement.executeUpdate();

            // If the insert was successful, retrieve the generated articleID
            if (rowsUpdated > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedArticleID = generatedKeys.getInt(1);
                    article.setArticleID(generatedArticleID); // Set the generated articleID in the Article object
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isDuplicateArticle(String url) {
        String sql = "SELECT COUNT(*) FROM article WHERE url = ?";
        try (Connection dbConnection = MySQLConnection.connectToDatabase();
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

    private static Timestamp parsePublishedDate(String publishedDate) {
        try {
            return Timestamp.valueOf(publishedDate.replace("T", " ").replace("Z", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        List<Article> articles = fetchArticles();
        articles.forEach(System.out::println);
    }

}


