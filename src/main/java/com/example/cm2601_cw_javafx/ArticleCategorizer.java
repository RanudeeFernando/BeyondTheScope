package com.example.cm2601_cw_javafx;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ArticleCategorizer {
    private static final String API_KEY = "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";
    private static final String MODEL_URL = "https://api-inference.huggingface.co/models/ranudee/news-category-classifier";


    // Method to classify a single article
    public Category classifyArticle(String articleText) throws Exception {
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("inputs", articleText);

        HttpResponse<String> response = sendRequestToHuggingFace(jsonRequest);

        String responseBody = response.body();

        // Print the raw response
        System.out.println("Raw API Response: " + responseBody + "\n");

        try {
            JSONArray jsonResponseArray = new JSONArray(responseBody);

            // The first element in the array is an array of label/score pairs
            JSONArray labelScores = jsonResponseArray.getJSONArray(0);  // Get the first inner array

            // Find the label with the highest score
            String predictedCategory = "";
            double highestScore = 0.0;

            for (int i = 0; i < labelScores.length(); i++) {
                JSONObject labelScore = labelScores.getJSONObject(i);
                String label = labelScore.getString("label");
                double score = labelScore.getDouble("score");

                if (score > highestScore) {
                    highestScore = score;
                    predictedCategory = label;
                }
            }

            return mapStringToCategory(predictedCategory);

        } catch (Exception e) {
            // Handle the case where the response is not valid JSON
            System.out.println("Error parsing JSON response: " + e.getMessage() + "\n");
            return Category.UNKNOWN;
        }
    }

    // Method to map the string category to the Category enum
    private Category mapStringToCategory(String category) {
        switch (category) {
            case "LABEL_0":
                return Category.WORLD;
            case "LABEL_1":
                return Category.SPORTS;
            case "LABEL_2":
                return Category.BUSINESS;
            case "LABEL_3":
                return Category.SCIENCE;
            default:
                return Category.UNKNOWN;
        }
    }

    // Method to update the articles in the database with their predicted categories
    public void categorizeArticles(List<Article> articles) {
        for (Article article : articles) {
            try {
                Category predictedCategory = classifyArticle(article.getContent());
                article.setCategory(predictedCategory);  // Set the category as the enum

                updateArticleCategoryInDatabase(article);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private HttpResponse<String> sendRequestToHuggingFace(JSONObject jsonRequest) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MODEL_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest.toString()))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    private void updateArticleCategoryInDatabase(Article article) {
        String sql = "UPDATE article SET category = ? WHERE articleID = ?";

        try (Connection dbConnection = MySQLConnection.connectToDatabase();
             PreparedStatement statement = dbConnection.prepareStatement(sql)) {

            statement.setString(1, article.getCategory().name());
            statement.setInt(2, article.getArticleID());

            int rowsUpdated = statement.executeUpdate();

            // Check if the update was successful
            if (rowsUpdated > 0) {
                System.out.println("Article category updated successfully.");
            } else {
                System.out.println("No article found with the given ID.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
