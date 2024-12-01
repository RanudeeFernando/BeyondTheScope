package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.model.Article;
import com.example.cm2601_cw_javafx.service.SystemUserManager;
import com.example.cm2601_cw_javafx.db.UserDBManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class ViewLikedArticlesController extends BaseController {
    @FXML
    private ImageView imageViewLogo;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private ListView<String> likedArticleListView;

    // private final RegularUserManager regularUserManager = new RegularUserManager();

    private final UserDBManager userDBManager = new UserDBManager();
    private final SystemUserManager systemUserManager = new SystemUserManager(userDBManager);




    public void initialize(){

    }

    // Method to initialize and load liked articles
    public void initializeUserLikedArticles(int userId) {

        List<Article> likedArticles = systemUserManager.getLikedArticles(userId);

        for (Article article : likedArticles) {
            likedArticleListView.getItems().add(article.getTitle());
        }

        likedArticleListView.setOnMouseClicked((MouseEvent event) -> {
            String selectedTitle = likedArticleListView.getSelectionModel().getSelectedItem();
            Article selectedArticle = likedArticles.stream()
                    .filter(article -> article.getTitle().equals(selectedTitle))
                    .findFirst()
                    .orElse(null);

            if (selectedArticle != null) {
                openArticleDetails(selectedArticle);
            }

        });



    }

    private void openArticleDetails(Article article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/view-full-article.fxml"));
            Parent root = loader.load();

            ViewFullArticleController controller = loader.getController();
            controller.setArticleDetails(article);

            Scene currentScene = likedArticleListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goBackToHome() {
        try {
            // Load the home page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
            e.printStackTrace();
        }
    }
}
