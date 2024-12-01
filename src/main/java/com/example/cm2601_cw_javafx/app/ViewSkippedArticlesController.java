package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Article;
import com.example.cm2601_cw_javafx.model.User;
import com.example.cm2601_cw_javafx.model.UserSession;
import com.example.cm2601_cw_javafx.service.SystemUserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.List;

public class ViewSkippedArticlesController extends BaseController {

    @FXML
    private ImageView imageViewLogo;

    @FXML
    private ListView<String> skippedArticlesListView;

    //private final UserSession userSession = UserSession.getInstance();

    User user = (User) UserSession.getInstance().getLoggedInUser();

    //private final RegularUserManager regularUserManager = new RegularUserManager();

    private final DBManager DBManager = new DBManager();
    private final SystemUserManager systemUserManager = new SystemUserManager(DBManager);

    public void initialize() {
        loadSkippedArticles();
    }

    private void loadSkippedArticles() {
        int currentUserId = user.getUserID();
        List<Article> skippedArticles = systemUserManager.getSkippedArticles(currentUserId);

        for (Article article : skippedArticles) {
            skippedArticlesListView.getItems().add(article.getTitle());
        }

        skippedArticlesListView.setOnMouseClicked((MouseEvent event) -> {
            String selectedTitle = skippedArticlesListView.getSelectionModel().getSelectedItem();
            Article selectedArticle = skippedArticles.stream()
                    .filter(article -> article.getTitle().equals(selectedTitle))
                    .findFirst()
                    .orElse(null);

            if (selectedArticle != null) {
                openArticleDetails(selectedArticle);
            }

            HomeController homeController = new HomeController();
            homeController.addArticleToViewedHistory(selectedArticle);
        });
    }

    private void openArticleDetails(Article article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/view-full-article.fxml"));
            Parent root = loader.load();

            ViewFullArticleController controller = loader.getController();
            controller.setArticleDetails(article);

            Scene currentScene = skippedArticlesListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goBackToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            Scene currentScene = skippedArticlesListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
            e.printStackTrace();
        }
    }
}


