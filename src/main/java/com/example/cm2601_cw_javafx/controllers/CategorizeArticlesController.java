package com.example.cm2601_cw_javafx.controllers;

import com.example.cm2601_cw_javafx.Article;
import com.example.cm2601_cw_javafx.ArticleCategorizer;
import com.example.cm2601_cw_javafx.ArticleService;
import com.example.cm2601_cw_javafx.Category;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class CategorizeArticlesController {
    @FXML
    private TextArea logArea;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button categorizeArticlesButton;


//    public void startCategorization() {
//
//        new Thread(() -> {
//            ArticleCategorizer categorizer = new ArticleCategorizer();
//            ArticleService articleService = new ArticleService();
//
//            List<Article> unknownArticles = articleService.getArticlesWithUnknownCategory();
//
//            if (unknownArticles.isEmpty()) {
//                appendLog("No articles with 'UNKNOWN' category to categorize.");
//                return;
//            }
//
//            int processedCount = 0;
//            int errorCount = 0;
//            int defaultedToUnknownCount = 0;
//
//            for (Article article : unknownArticles) {
//                try {
//                    // Categorize article
//                    Category predictedCategory = categorizer.classifyArticle(article.getContent());
//
//                    // Check if the category is UNKNOWN
//                    if (predictedCategory == Category.UNKNOWN) {
//                        appendLog("Article \"" + article.getTitle() + "\" defaulted to UNKNOWN.");
//                        defaultedToUnknownCount++;
//                    } else {
//                        appendLog("Successfully categorized: \"" + article.getTitle() + "\" as " + predictedCategory + ".");
//                        processedCount++;
//                    }
//
//                    // Update the article in the database regardless of the outcome
//                    article.setCategory(predictedCategory);
//                    articleService.updateArticleCategoryInDatabase(article);
//
//                } catch (Exception e) {
//                    // Log any errors
//                    appendLog("Error processing article: \"" + article.getTitle() + "\" - " + e.getMessage());
//                    errorCount++;
//                }
//            }
//
//            // Log completion summary
//            appendLog("Categorization complete. Successfully categorized: " + processedCount +
//                    ", Defaulted to UNKNOWN: " + defaultedToUnknownCount + ", Errors: " + errorCount);
//        }).start();
//    }

    public void startCategorization() {
        categorizeArticlesButton.setDisable(true); // Disable the button at the start

        new Thread(() -> {
            ArticleCategorizer categorizer = new ArticleCategorizer();
            ArticleService articleService = new ArticleService();

            List<Article> unknownArticles = articleService.getArticlesWithUnknownCategory();

            if (unknownArticles.isEmpty()) {
                appendLog("No articles with 'UNKNOWN' category to categorize.");
                Platform.runLater(() -> categorizeArticlesButton.setDisable(false)); // Re-enable the button
                return;
            }

            int processedCount = 0;
            int errorCount = 0;
            int defaultedToUnknownCount = 0;

            for (Article article : unknownArticles) {
                try {
                    Category predictedCategory = categorizer.classifyArticle(article.getContent());

                    if (predictedCategory == Category.UNKNOWN) {
                        appendLog("Article \"" + article.getTitle() + "\" defaulted to UNKNOWN.");
                        defaultedToUnknownCount++;
                    } else {
                        appendLog("Successfully categorized: \"" + article.getTitle() + "\" as " + predictedCategory + ".");
                        processedCount++;
                    }

                    article.setCategory(predictedCategory);
                    articleService.updateArticleCategoryInDatabase(article);

                } catch (Exception e) {
                    appendLog("Error processing article: \"" + article.getTitle() + "\" - " + e.getMessage());
                    errorCount++;
                }
            }

            appendLog("Categorization complete. Successfully categorized: " + processedCount +
                    ", Defaulted to UNKNOWN: " + defaultedToUnknownCount + ", Errors: " + errorCount);

            // Re-enable the button after completion
            Platform.runLater(() -> categorizeArticlesButton.setDisable(false));

        }).start();
    }


    private void appendLog(String message) {
        Platform.runLater(() -> logArea.appendText(message + "\n"));
    }

    @FXML
    private void goBackToDashboard() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/admin-dashboard.fxml"));
            Parent root = loader.load();

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Admin Dashboard.");
            e.printStackTrace();
        }
    }
}
