package com.example.cm2601_cw_javafx.app;


import com.example.cm2601_cw_javafx.model.User;
import com.example.cm2601_cw_javafx.model.ViewedArticle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class ViewHistoryControllerUser extends UserBaseController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ListView<String> articleListView;

    private User currentUser;

    // Sets current user
    @Override
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;

    }


    public void initializeUserViewHistory(int userID) {

        try {
            // Retrieve the viewed articles for the user
            List<ViewedArticle> viewedArticles = currentUser.getViewedArticles(userID);

            if (viewedArticles.isEmpty()) {
                // Show an alert if the view history is empty
                Platform.runLater(() -> showAlert("It seems you haven't viewed any articles yet. Start exploring articles, and your view history will be updated here."));
                return;
            }

            // Directly populate the ListView with article data
            for (ViewedArticle article : viewedArticles) {
                articleListView.getItems().add(article.toString());
            }

        } catch (Exception e) {
            System.out.println("An unexpected error occurred while initializing the view history: " + e.getMessage());

        }

    }


    // Method to return to Home page
    public void goBackToHome() {
        try {
            // Load the home page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            HomeControllerUser controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");

        }
    }

}
