package com.example.cm2601_cw_javafx.app;


import com.example.cm2601_cw_javafx.model.Article;
import com.example.cm2601_cw_javafx.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class ViewLikedArticlesController extends UserBaseController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ListView<String> likedArticleListView;

    private User user;

    @Override
    public void setCurrentUser(User currentUser) {
        this.user = currentUser;
    }


    // Initializes the list of liked articles for the user
    public void initializeUserLikedArticles(int userID) {

        try {
            // Retrieve the liked articles for the user
            List<Article> likedArticles = user.getLikedArticles(userID);

            if (likedArticles.isEmpty()) {
                // Show an alert if the liked articles list is empty
                Platform.runLater(() -> showAlert("It seems you haven't liked any articles yet. Start liking articles, and they'll appear here!"));
                return;
            }

            // Directly add each article title to the ListView
            for (int i = 0; i < likedArticles.size(); i++) {
                likedArticleListView.getItems().add(likedArticles.get(i).getTitle());
            }

        } catch (Exception e) {
            System.out.println("An unexpected error occurred while initializing the liked articles: " + e.getMessage());
        }
    }

    // Navigates back to the Home page
    public void goBackToHome() {
        try {
            // Load the home page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            HomeController homeController = loader.getController();
            homeController.setCurrentUser(user);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
        }
    }
}
