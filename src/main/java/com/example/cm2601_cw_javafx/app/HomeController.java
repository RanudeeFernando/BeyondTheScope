package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Article;
import com.example.cm2601_cw_javafx.model.User;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController extends UserBaseController {
    @FXML
    private ListView<String> articleListView;
    @FXML
    private Label welcomeLabel;
    private User currentUser;

    // Sets the current user for this controller
    @Override
    public void setCurrentUser(User currentUser) {
        super.setCurrentUser(currentUser);
        this.currentUser = currentUser;
    }

    // Initializes the controller
    public void initialize() {
        // Set the welcome label with username
        Platform.runLater(() -> welcomeLabel.setText("Hello, " + currentUser.getUsername() + "!"));

        // Load articles into the ListView
        loadArticles();

        // Handle double-click on articles to view the full article
        articleListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedTitle = articleListView.getSelectionModel().getSelectedItem();
                if (selectedTitle != null) {

                    Article selectedArticle = DBManager.getAllArticlesQuery()
                            .stream()
                            .filter(article -> article.getTitle().equals(selectedTitle))
                            .findFirst()
                            .orElse(null);

                    if (selectedArticle != null) {
                        viewFullArticle(selectedArticle);
                    }

                    addArticleToViewedHistory(selectedArticle);
                }
            }
        });
    }

    // Loads all articles into the ListView
    private void loadArticles() {
        List<Article> articles = DBManager.getAllArticlesQuery();
        List<String> titles = articles.stream()
                .map(Article::getTitle)
                .collect(Collectors.toList());
        articleListView.setItems(FXCollections.observableArrayList(titles));
    }

    // Adds the selected article to the user's viewed history
    public void addArticleToViewedHistory(Article article) {
        int userId = currentUser.getUserID();
        DBManager.insertViewedArticleQuery(userId, article.getArticleID());

    }

    // Redirects the user to view the full article
    private void viewFullArticle(Article article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/view-full-article.fxml"));
            Parent root = loader.load();


            ViewFullArticleController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            controller.setArticleDetails(article);

            Scene currentScene = articleListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Full Article page.");
        }
    }

    // Redirects to the View History page
    @FXML
    private void onViewHistoryMenuItemClicked() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/view-history.fxml"));
            Parent root = loader.load();

            ViewHistoryController controller = loader.getController();

            controller.setCurrentUser(currentUser);
            controller.initializeUserViewHistory(currentUser.getUserID());

            Scene currentScene = articleListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to View History page.");

        }
    }

    // Redirects to the View Liked Articles page
    @FXML
    private void onViewLikedArticlesMenuItemClicked() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/view-liked-articles.fxml"));
            Parent root = loader.load();

            ViewLikedArticlesController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            controller.initializeUserLikedArticles(currentUser.getUserID());

            Scene currentScene = articleListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to View Liked Articles page.");

        }
    }

    // Redirects to the View Skipped Articles page
    @FXML
    private void onViewSkippedArticlesMenuItemClicked() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/view-skipped-articles.fxml"));
            Parent root = loader.load();

            ViewSkippedArticlesController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            controller.initializeSkippedArticles(currentUser.getUserID());

            Scene currentScene = articleListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to View Skipped Articles page.");

        }
    }

    // Logs out the current user and redirects to the login page
    @FXML
    private void handleLogout() {

        try {
            showAlert("Logout successful! You will be directed to the Main page soon.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/main.fxml"));
            Parent root = loader.load();

            Scene currentScene = articleListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("Error while navigating to the login page.");

        }
    }

    // Redirects to the get recommendations page
    @FXML
    private void handleViewRecommendations() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/get-recommendations.fxml"));
        Parent root = loader.load();

        GetRecommendationsController controller = loader.getController();

        controller.setCurrentUser(currentUser);
        controller.initializeRecommendedArticles();

        Stage stage = (Stage) articleListView.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Redirects to the Update Profile page
    @FXML
    private void onUpdateProfileMenuItemClicked() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/update-profile.fxml"));
            Parent root = loader.load();

            UpdateProfileController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            controller.initializeUserDetails();

            Scene currentScene = articleListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Update Profile page.");

        }
    }



}
