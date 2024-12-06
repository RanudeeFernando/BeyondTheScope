package com.example.cm2601_cw_javafx.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.example.cm2601_cw_javafx.model.Category;
import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Article;
import org.json.JSONArray;
import org.json.JSONObject;

public class ArticleCategorizer {
    private static final String API_KEY = "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";
    private static final String MODEL_URL = "https://api-inference.huggingface.co/models/cardiffnlp/twitter-roberta-base-topic-latest";


    // Method to classify a single article
    public Category classifyArticle(String articleText) throws Exception {
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("inputs", articleText);

        HttpResponse<String> response = sendRequestToHuggingFace(jsonRequest);

        String responseBody = response.body();

        // Print the raw response
        System.out.println("\nRaw API Response: " + responseBody);

        try {
            JSONArray jsonResponseArray = new JSONArray(responseBody);

            // The first element in the array is an array of label/score pairs
            JSONArray labelScores = jsonResponseArray.getJSONArray(0);  // Get the first inner array

            // Finding label with the highest score
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


    // Maps a label string to a category
    private Category mapStringToCategory(String label) {
        switch (label.toLowerCase()) {
            case "arts_&_culture":
            case "music":
            case "film_tv_&_video":
            case "celebrity_&_pop_culture":
            case "fashion_&_style":
            case "gaming":
                return Category.ENTERTAINMENT;

            case "business_&_entrepreneurs":
                return Category.BUSINESS;

            case "learning_&_educational":
            case "youth_&_student_life":
                return Category.EDUCATION;

            case "food_&_dining":
            case "fitness_&_health":
            case "family":
            case "relationships":
            case "diaries_&_daily_life":
            case "other_hobbies":
            case "travel_&_adventure":
                return Category.HEALTH_LIFESTYLE;

            case "news_&_social_concern":
                return Category.POLITICS;

            case "science_&_technology":
                return Category.SCIENCE_TECH;

            case "sports":
                return Category.SPORTS;

            default:
                return Category.UNKNOWN;
        }
    }


    // Sends an HTTP POST request to the Hugging Face API
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


    // Categorizes all articles with in "UNKNOWN" category
    public void categorizeUnknownArticles() {
        List<Article> unknownArticles;

        do {
            unknownArticles = DBManager.getArticlesWithUnknownCategoryQuery();

            if (unknownArticles.isEmpty()) {
                System.out.println("\nNo articles with 'UNKNOWN' category to categorize.");
                break;
            }

            for (Article article : unknownArticles) {
                try {
                    Category predictedCategory = classifyArticle(article.getContent());
                    article.setCategory(predictedCategory);

                    // Update the article in the database
                    DBManager.updateArticleCategoryQuery(article);
                    System.out.println("Article \"" + article.getTitle() + "\" categorized as " + predictedCategory);

                } catch (Exception e) {
                    System.out.println("Error processing article \"" + article.getTitle() + "\": " + e.getMessage());
                }
            }
        } while (!unknownArticles.isEmpty());

        System.out.println("Categorization complete. All articles have been processed.");
    }


}
