package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Article;
import com.example.cm2601_cw_javafx.model.User;
import com.example.cm2601_cw_javafx.service.SystemUserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ViewLikedArticlesController extends BaseController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ListView<String> likedArticleListView;

    private final DBManager dbManager = new DBManager();

    //private final SystemUserManager systemUserManager = new SystemUserManager(dbManager);

    User user;

    @Override
    public void setUser(User user) {
        this.user = user;
    }


    // Method to initialize and load liked articles
    public void initializeUserLikedArticles(int userId) {

        List<Article> likedArticles;
        try {
            likedArticles = user.getLikedArticles(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Article article : likedArticles) {
            likedArticleListView.getItems().add(article.getTitle());
        }

    }


    public void goBackToHome() {
        try {
            // Load the home page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            HomeController homeController = loader.getController();
            homeController.setUser(user);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
            e.printStackTrace();
        }
    }
}
