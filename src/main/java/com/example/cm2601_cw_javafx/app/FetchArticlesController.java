package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Admin;
import com.example.cm2601_cw_javafx.model.Article;
import com.example.cm2601_cw_javafx.service.ArticleFetcher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class FetchArticlesController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextArea logArea;
    @FXML
    private Button fetchArticlesButton;

    Admin admin;
    public void setAdmin(Admin admin) {
        this.admin = admin;

    }

    @FXML
    private void handleFetchArticles() {
        fetchArticlesButton.setDisable(true);

        new Thread(() -> {
            try {
                appendLog("Starting article fetching process...");
                List<Article> articles = admin.fetchArticlesManually();

                if (articles.isEmpty()) {
                    appendLog("No new articles were fetched from the API.");
                } else {
                    //ArticleFetcher.saveArticles(articles);
                    for (Article article : articles) {
                        try {
                            DBManager.addArticleToDatabase(article);
                        } catch (Exception e) {
                            System.err.println("Failed to save article: " + article.getTitle());
                            e.printStackTrace();
                        }
                    }
                    appendLog("Fetched " + articles.size() + " articles. Saving to database...");


                    Platform.runLater(() -> {
                        for (Article article : articles) {
                            String logMessage = "Title: " + article.getTitle() + " | Published Date: " + article.getPublishedDate();
                            appendLog("Saved: " + logMessage);
                        }
                    });

                    appendLog("Successfully fetched and saved " + articles.size() + " articles.");
                }
            } catch (Exception e) {
                appendLog("An error occurred while fetching articles: " + e.getMessage());
                e.printStackTrace();

            } finally {
                Platform.runLater(() -> fetchArticlesButton.setDisable(false));
            }

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

            AdminDashboardController controller = loader.getController();
            controller.setAdmin(admin);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Admin Dashboard.");
            e.printStackTrace();
        }
    }
}

