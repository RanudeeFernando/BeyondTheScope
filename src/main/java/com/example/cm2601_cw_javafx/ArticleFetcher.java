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
    // Replace with your actual News API key
    private static final String API_KEY = "829613513f4a4c9794a7ecfc44a91b0c";
    private static final String API_URL = "https://newsapi.org/v2/top-headlines?language=en&pageSize=10&apiKey=" + API_KEY;

    public static List<String> fetchArticles() {
        List<String> articleTitles = new ArrayList<>();
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
                JSONArray articles = jsonResponse.getJSONArray("articles");

                for (int i = 0; i < articles.length(); i++) {
                    JSONObject article = articles.getJSONObject(i);
                    String title = article.optString("title", null);
                    String sourceName = article.getJSONObject("source").optString("name", null);
                    String author = article.optString("author", null);
                    String content = article.optString("content", null);
                    String urlField = article.optString("url", null);
                    String publishedDate = article.optString("publishedAt", null);

                    // Check if title and source are present before proceeding
                    if (isValidArticle(title, sourceName, author, content, urlField) && !isDuplicateArticle(urlField)) {
                        articleTitles.add(title);
                        addArticleToDatabase(title, sourceName, author, content, urlField, publishedDate);
                    }
                }
            } else {
                System.out.println("Failed to fetch articles. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articleTitles;
    }


    private static boolean isValidArticle(String title, String sourceName, String author, String content, String url) {
        return title != null && !title.trim().isEmpty() && !title.equalsIgnoreCase("[Removed]") &&
                sourceName != null && !sourceName.trim().isEmpty() && !sourceName.equalsIgnoreCase("[Removed]") &&
                url != null && !url.trim().isEmpty() &&
                (author == null || !author.trim().equalsIgnoreCase("[Removed]")) &&  // Allow null but check for "[Removed]"
                content != null && !content.trim().isEmpty() && !content.equalsIgnoreCase("[Removed]"); // Ensure content is not null or empty
    }


    private static void addArticleToDatabase(String title, String source, String author, String content, String url, String publishedDate) {
        String sql = "INSERT INTO article (title, source, author, content, url, publishedDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection dbConnection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql)) {

            statement.setString(1, title);
            statement.setString(2, source);
            statement.setString(3, author);
            statement.setString(4, content);
            statement.setString(5, url);
            statement.setTimestamp(6, parsePublishedDate(publishedDate));
            statement.executeUpdate();

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
        List<String> articles = fetchArticles();
        articles.forEach(System.out::println);
    }

}


