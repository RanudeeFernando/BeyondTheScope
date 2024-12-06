package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.model.Article;
import com.example.cm2601_cw_javafx.model.User;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ViewSkippedArticlesControllerUser extends UserBaseController {

    @FXML
    private ListView<String> skippedArticlesListView;

    private User currentUser;

    // Sets the current user for this controller
    @Override
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    // Initializes the list of skipped articles for the user
    public void initializeSkippedArticles(int userID) {

        try {
            // Retrieve the skipped articles for the user
            List<Article> skippedArticles = currentUser.getSkippedArticles(userID);

            if (skippedArticles.isEmpty()) {
                // Show an alert if the skipped articles list is empty
                Platform.runLater(() -> showAlert("It seems you haven't skipped any articles yet. Your skipped articles will appear here!"));
                return;
            }

            // Directly populate the ListView with skipped article titles
            for (Article article : skippedArticles) {
                skippedArticlesListView.getItems().add(article.getTitle());
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving skipped articles: " + e.getMessage());
        }
    }

    // Navigates back to the Home page
    public void goBackToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            HomeControllerUser controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Scene currentScene = skippedArticlesListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
        }
    }
}


