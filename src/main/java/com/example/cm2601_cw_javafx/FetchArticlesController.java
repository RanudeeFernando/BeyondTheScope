package com.example.cm2601_cw_javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class FetchArticlesController {
    @FXML
    private ListView<String> articleListView;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private void initialize() {
        // Handle double-click event to show article details
        articleListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click
                String selectedItem = articleListView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    showAlert("Article Details:\n" + selectedItem);
                }
            }
        });
    }

    @FXML
    private void handleFetchArticles() {
        try {
            List<Article> articles = ArticleFetcher.fetchArticles();

            if (articles.isEmpty()) {
                showAlert("No new articles were fetched from the API.");
            } else {
                ArticleFetcher.saveArticles(articles);

                articleListView.getItems().clear();
                for (Article article : articles) {
                    articleListView.getItems().add("Title: " + article.getTitle() + " | Published Date: " + article.getPublishedDate());
                }

                showAlert("Successfully fetched and saved " + articles.size() + " articles.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("An error occurred while fetching articles. Please try again.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goBackToDashboard() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-dashboard.fxml"));
            Parent root = loader.load();

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Admin Dashboard.");
            e.printStackTrace();
        }
    }
}

