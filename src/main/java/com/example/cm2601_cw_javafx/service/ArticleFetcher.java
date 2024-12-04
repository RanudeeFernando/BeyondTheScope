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

    public static List<Article> fetchArticles() {
        List<Article> articles = new ArrayList<>();

        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();

                String inputLine;
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

                    if (isValidArticle(title, sourceName, author, content, urlField) && !DBManager.isDuplicateArticleQuery(urlField)) {
                        Timestamp publishedDateTimestamp = parsePublishedDate(publishedDate);
                        Category category = Category.UNKNOWN;

                        Article article = new Article(0, title, content, category, author, sourceName, urlField, publishedDateTimestamp);
                        articles.add(article);
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

    public static void saveArticles(List<Article> articles) {
        for (Article article : articles) {
            try {
                DBManager.insertArticleQuery(article);
            } catch (Exception e) {
                System.err.println("Failed to save article: " + article.getTitle());
                e.printStackTrace();
            }
        }
    }

    private static boolean isValidArticle(String title, String sourceName, String author, String content, String url) {
        return title != null && !title.trim().isEmpty() && !title.equalsIgnoreCase("[Removed]") &&
                sourceName != null && !sourceName.trim().isEmpty() && !sourceName.equalsIgnoreCase("[Removed]") &&
                url != null && !url.trim().isEmpty() && !url.equalsIgnoreCase("[Removed]") &&
                author != null && !author.trim().isEmpty() && !author.trim().equalsIgnoreCase("[Removed]") &&
                content != null && !content.trim().isEmpty() && !content.equalsIgnoreCase("[Removed]");
    }



    private static Timestamp parsePublishedDate(String publishedDate) {
        try {
            return Timestamp.valueOf(publishedDate.replace("T", " ").replace("Z", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


//    public static void main(String[] args) {
//        List<Article> articles = fetchArticles();
//        saveArticles(articles);
//        articles.forEach(System.out::println);
//    }

}


