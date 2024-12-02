package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Article;
import com.example.cm2601_cw_javafx.model.User;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
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

    //User loggedInUser = (User) SessionService.getInstance().getLoggedInUser();

    User loggedInUser;

    @Override
    public void setUser(User user) {
        super.setUser(user);
        this.loggedInUser = user;
        initializeUserSpecificData();
    }

    private void initializeUserSpecificData() {
        if (loggedInUser != null) {
            System.out.println("User set in HomeController: " + loggedInUser.getUsername());
            loadArticles();
        } else {
            System.err.println("Error: User is null in HomeController.");
        }
    }


//    public void initialize() {
//
//        loadArticles();
//
//        int userID = loggedInUser.getUserID();
//        String name = loggedInUser.getUsername();
//
//        System.out.println(name);
//
//
//        // Set up double-click event for ListView items
//        articleListView.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 2) {
//                String selectedTitle = articleListView.getSelectionModel().getSelectedItem();
//                if (selectedTitle != null) {
//
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

//    private void loadArticles() {
//        int userID = loggedInUser.getUserID();
//        List<Article> articles = DBManager.getAllArticles(userID);
//        List<String> titles = articles.stream()
//                .map(Article::getTitle)
//                .collect(Collectors.toList());
//        articleListView.setItems(FXCollections.observableArrayList(titles));
//    }



    public void initialize() {

        loadArticles();


        articleListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedTitle = articleListView.getSelectionModel().getSelectedItem();
                if (selectedTitle != null) {

                    Article selectedArticle = DBManager.getAllArticles()
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
        List<Article> articles = DBManager.getAllArticles();
        List<String> titles = articles.stream()
                .map(Article::getTitle)
                .collect(Collectors.toList());
        articleListView.setItems(FXCollections.observableArrayList(titles));
    }

    public void addArticleToViewedHistory(Article article) {
        int userId = loggedInUser.getUserID();
        DBManager.addViewedArticle(userId, article.getArticleID());

    }


    private void viewFullArticle(Article article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/view-full-article.fxml"));
            Parent root = loader.load();


            ViewFullArticleController controller = loader.getController();
            controller.setUser(loggedInUser);
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

            controller.setUser(loggedInUser);
            controller.initializeUserViewHistory(loggedInUser.getUserID());

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
            controller.setUser(loggedInUser);

            controller.initializeUserLikedArticles(loggedInUser.getUserID());

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
            controller.setUser(loggedInUser);
            controller.initializeSkippedArticles(loggedInUser.getUserID());

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
        //SessionService.getInstance().clearSession();

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
        //controller.setLoggedInUserId(loggedInUser.getUserID());
        controller.setUser(loggedInUser);
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
            controller.setUser(loggedInUser);
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
