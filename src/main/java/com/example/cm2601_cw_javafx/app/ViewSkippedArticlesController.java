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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ViewSkippedArticlesController extends BaseController {

    @FXML
    private ListView<String> skippedArticlesListView;

    User user;
    private final DBManager dbManager = new DBManager();
    private final SystemUserManager systemUserManager = new SystemUserManager(dbManager);

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    public void initializeSkippedArticles(int userID) {

        List<Article> skippedArticles;
        try {
            skippedArticles = user.getSkippedArticles(userID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Article article : skippedArticles) {
            skippedArticlesListView.getItems().add(article.getTitle());
        }


    }

    public void goBackToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            HomeController controller = loader.getController();
            controller.setUser(user);

            Scene currentScene = skippedArticlesListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
            e.printStackTrace();
        }
    }
}


