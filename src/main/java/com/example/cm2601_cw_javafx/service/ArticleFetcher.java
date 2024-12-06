package com.example.cm2601_cw_javafx.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import com.example.cm2601_cw_javafx.db.DBManager;

import com.example.cm2601_cw_javafx.model.Category;
import com.example.cm2601_cw_javafx.model.Article;
import org.json.JSONArray;
import org.json.JSONObject;

public class ArticleFetcher {
    private static final String API_KEY = "829613513f4a4c9794a7ecfc44a91b0c";
    private static final String API_URL = "https://newsapi.org/v2/top-headlines?language=en&pageSize=7&apiKey=" + API_KEY;


    // Fetches articles from the News API and returns a list of valid articles
    public static List<Article> fetchArticles() {
        List<Article> articles = new ArrayList<>();

        try {
            // Set up the URL and establish an HTTP connection
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            // Check if the response is successful
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();

                // Read the API response line by line
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                reader.close();

                // Parse the API response JSON
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray articlesJsonArray = jsonResponse.getJSONArray("articles");

                // Process each article in the JSON array
                for (int i = 0; i < articlesJsonArray.length(); i++) {

                    // Extract relevant fields from the JSON object
                    JSONObject articleJson = articlesJsonArray.getJSONObject(i);
                    String title = articleJson.optString("title", null);
                    String sourceName = articleJson.getJSONObject("source").optString("name", null);
                    String author = articleJson.optString("author", null);
                    String content = articleJson.optString("content", null);
                    String urlField = articleJson.optString("url", null);
                    String publishedDate = articleJson.optString("publishedAt", null);


                    // Validate and check for duplicate articles before adding to the list
                    if (isValidArticle(title, sourceName, author, content, urlField) && !DBManager.isDuplicateArticleQuery(urlField)) {
                        Timestamp publishedDateTimestamp = parsePublishedDate(publishedDate);
                        Category category = Category.UNKNOWN;

                        Article article = new Article(0, title, content, category, author, sourceName, urlField, publishedDateTimestamp);
                        articles.add(article);
                    }
                }
            } else {
                // Log an error if the API request fails
                System.out.println("Failed to fetch articles. Response code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return articles;
    }


    // Validates an article fields to ensure it meets criteria for storage
    private static boolean isValidArticle(String title, String sourceName, String author, String content, String url) {
        return title != null && !title.trim().isEmpty() && !title.equalsIgnoreCase("[Removed]") &&
                sourceName != null && !sourceName.trim().isEmpty() && !sourceName.equalsIgnoreCase("[Removed]") &&
                url != null && !url.trim().isEmpty() && !url.equalsIgnoreCase("[Removed]") &&
                author != null && !author.trim().isEmpty() && !author.trim().equalsIgnoreCase("[Removed]") &&
                content != null && !content.trim().isEmpty() && !content.equalsIgnoreCase("[Removed]");
    }


    // Parses the published date string into a SQL Timestamp format
    private static Timestamp parsePublishedDate(String publishedDate) {
        try {
            return Timestamp.valueOf(publishedDate.replace("T", " ").replace("Z", ""));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }


}


