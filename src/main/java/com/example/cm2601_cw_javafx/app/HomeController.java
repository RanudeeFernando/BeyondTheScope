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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController extends BaseController {
    @FXML
    private MenuItem logoutMenuItem;
    @FXML
    private ImageView imageViewLogo;
    @FXML
    private Menu homeMenu;
    @FXML
    private Menu profileMenu;
    @FXML
    private ListView<String> articleListView;
    @FXML
    private Label welcomeLabel;

    //private final DBManager DBManager = new DBManager();

    User currentUser;

    @Override
    public void setCurrentUser(User currentUser) {
        super.setCurrentUser(currentUser);
        this.currentUser = currentUser;
    }

    public void initialize() {

        Platform.runLater(() -> welcomeLabel.setText("Hello, " + currentUser.getUsername() + "!"));

        loadArticles();

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

    private void loadArticles() {
        List<Article> articles = DBManager.getAllArticlesQuery();
        List<String> titles = articles.stream()
                .map(Article::getTitle)
                .collect(Collectors.toList());
        articleListView.setItems(FXCollections.observableArrayList(titles));
    }

    public void addArticleToViewedHistory(Article article) {
        int userId = currentUser.getUserID();
        DBManager.insertViewedArticleQuery(userId, article.getArticleID());

    }


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
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/main.fxml"));
            Parent root = loader.load();

            Scene currentScene = articleListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("Error while navigating to the login page.");
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }


















    //private User loggedInUser;

//    public void setUser(User user) {
//        if (user == null) {
//            System.err.println("Error: Attempted to set a null user in HomeController.");
//        } else {
//            this.loggedInUser = user;
//            System.out.println("User set in HomeController: " + user.getUsername());
//        }
//        postInitialize();
//    }


    // ---------------------------------------------

//    public void postInitialize() {
//        if (loggedInUser == null) {
//            System.err.println("Error: Logged-in user is null in postInitialize.");
//            System.out.println("An error occurred. Please log in again.");
//            return;
//        }
//
//        loadArticles();
//
//        int userID = loggedInUser.getUserID();
//        String name = loggedInUser.getUsername();
//
//        System.out.println("User in postInitialize: " + name);
//
//        // Set up double-click event for ListView items
//        articleListView.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 2) { // Detect double-click
//                String selectedTitle = articleListView.getSelectionModel().getSelectedItem();
//                if (selectedTitle != null) {
//                    // Find the Article object corresponding to the selected title
//                    Article selectedArticle = DBManager.getAllArticles(userID)
//                            .stream()
//                            .filter(article -> article.getTitle().equals(selectedTitle))
//                            .findFirst()
//                            .orElse(null);
//
//                    if (selectedArticle != null) {
//                        viewFullArticle(selectedArticle);
//                    }
//
//                    // Add the article to the user's viewed history
//                    addArticleToViewedHistory(selectedArticle);
//                }
//            }
//        });
//    }





}
