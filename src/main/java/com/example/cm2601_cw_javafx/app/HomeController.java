package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Article;
import com.example.cm2601_cw_javafx.model.User;
import com.example.cm2601_cw_javafx.model.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController extends BaseController {

    public MenuItem logoutMenuItem;
    @FXML
    private ImageView imageViewLogo;
    @FXML
    private Menu homeMenu;
    @FXML
    private Menu profileMenu;
    @FXML
    private ListView<String> articleListView; // Add ListView

    private final DBManager DBManager = new DBManager();

    User user = (User) UserSession.getInstance().getLoggedInUser();

//    private User loggedInUser;
//
//    public void setUser(User user) {
//        this.loggedInUser = user;
//
//    }

    public void initialize() {

        loadArticles();

        int userID = user.getUserID();
        String name = user.getUsername();


        // Set up double-click event for ListView items
        articleListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Detect double-click
                String selectedTitle = articleListView.getSelectionModel().getSelectedItem();
                if (selectedTitle != null) {
                    // Find the Article object corresponding to the selected title
                    Article selectedArticle = DBManager.getAllArticles(userID)
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
        int userID = user.getUserID();
        List<Article> articles = DBManager.getAllArticles(userID);
        List<String> titles = articles.stream()
                .map(Article::getTitle) // Extracting the title for display
                .collect(Collectors.toList());
        articleListView.setItems(FXCollections.observableArrayList(titles));
    }

    private void viewFullArticle(Article article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/view-full-article.fxml"));
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

    public void addArticleToViewedHistory(Article article) {
        int userId = user.getUserID();
        DBManager.addViewedArticle(userId, article.getArticleID());

    }


    // Event handler for "View History" menu item
    @FXML
    private void onViewHistoryMenuItemClicked() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/view-history.fxml"));
            Parent root = loader.load();

            ViewHistoryController controller = loader.getController();

            int userId = user.getUserID();

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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/view-liked-articles.fxml"));
            Parent root = loader.load();

            ViewLikedArticlesController controller = loader.getController();

            int userId = user.getUserID();

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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/view-skipped-articles.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/main.fxml"));
            Parent root = loader.load();

            // Set the new root for the current scene
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
        controller.setLoggedInUserId(user.getUserID());

        Stage stage = (Stage) articleListView.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void onUpdateProfileMenuItemClicked() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/update-profile.fxml"));
            Parent root = loader.load();

            Scene currentScene = articleListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Update Profile page.");
            e.printStackTrace();
        }
    }





}
