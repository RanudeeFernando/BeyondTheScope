package com.example.cm2601_cw_javafx;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController extends BaseController{

    public MenuItem logoutMenuItem;
    @FXML
    private ImageView imageViewLogo;
    @FXML
    private Menu homeMenu;
    @FXML
    private Menu profileMenu;
    @FXML
    private ListView<String> articleListView; // Add ListView

    private final ArticleService articleService = new ArticleService();
    private HistoryService historyService; // HistoryService instance

    public void initialize() {
        setLogoImage(imageViewLogo, "images/logo5.png");
        loadArticles();

        Connection connection = MySQLConnection.connectToDatabase();
        if (connection != null) {
            historyService = new HistoryService(connection);
        } else {
            System.out.println("Failed to connect to the database, history tracking won't work.");
        }

        int userID = UserSession.getInstance().getUserId();

        // Set up double-click event for ListView items
        articleListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Detect double-click
                String selectedTitle = articleListView.getSelectionModel().getSelectedItem();
                if (selectedTitle != null) {
                    // Find the Article object corresponding to the selected title
                    Article selectedArticle = articleService.getAllArticles(userID)
                            .stream()
                            .filter(article -> article.getTitle().equals(selectedTitle))
                            .findFirst()
                            .orElse(null);

                    if (selectedArticle != null) {
                        viewFullArticle(selectedArticle);
                    }

                    // Add the article to the user's viewed history
                    addArticleToViewedHistory(selectedArticle);
                }
            }
        });
    }

    private void loadArticles() {
        int userID = UserSession.getInstance().getUserId();
        List<Article> articles = articleService.getAllArticles(userID);
        List<String> titles = articles.stream()
                .map(Article::getTitle) // Extracting the title for display
                .collect(Collectors.toList());
        articleListView.setItems(FXCollections.observableArrayList(titles));
    }

    private void viewFullArticle(Article article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-full-article.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the article data
            ViewFullArticleController controller = loader.getController();
            controller.setArticleDetails(article);
            // controller.setLikeButtonStatus();

            Scene currentScene = articleListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Full Article page.");
            e.printStackTrace();
        }
    }

    private void addArticleToViewedHistory(Article article) {
        if (historyService != null) {
            int userId = UserSession.getInstance().getUserId();
            historyService.addViewedArticle(userId, article.getArticleID());
        } else {
            System.out.println("Cannot add to viewed history. HistoryService is not initialized.");
        }
    }


    // Event handler for "View History" menu item
    @FXML
    private void onViewHistoryMenuItemClicked() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-history.fxml"));
            Parent root = loader.load();

            ViewHistoryController controller = loader.getController();

            // Get the current logged-in user's ID from UserSession
            int userId = UserSession.getInstance().getUserId();

            // Initialize the view history with the current user ID
            controller.initializeUserViewHistory(userId);

            Scene currentScene = articleListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to View History page.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onViewLikedArticlesMenuItemClicked() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-liked-articles.fxml"));
            Parent root = loader.load();

            ViewLikedArticlesController controller = loader.getController();

            int userId = UserSession.getInstance().getUserId();

            controller.initializeUserLikedArticles(userId);

            Scene currentScene = articleListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to View Liked Articles page.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onViewSkippedArticlesMenuItemClicked() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-skipped-articles.fxml"));
            Parent root = loader.load();

            Scene currentScene = articleListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to View Skipped Articles page.");
            e.printStackTrace();
        }
    }

    // Event handler for "Logout" menu item
    @FXML
    private void handleLogout() {
        // Clear the session
        UserSession.getInstance().clearSession();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            Parent root = loader.load();

            // Set the new root for the current scene
            Scene currentScene = articleListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("Error while navigating to the login page.");
            e.printStackTrace();
        }
    }


}
