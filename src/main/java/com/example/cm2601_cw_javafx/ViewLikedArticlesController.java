package com.example.cm2601_cw_javafx;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ViewLikedArticlesController extends BaseController{
    @FXML
    private ImageView imageViewLogo;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private ListView<String> likedArticleListView;

    private final UserManager userManager = new UserManager();

    public void initialize(){
        setLogoImage(imageViewLogo, "images/logo5.png");
    }

    // Method to initialize and load liked articles
    public void initializeUserLikedArticles(int userId) {

        List<Article> likedArticles = userManager.getLikedArticles(userId);

        List<String> titles = likedArticles.stream()
                .map(Article::getTitle)  // Get article titles
                .collect(Collectors.toList());

        likedArticleListView.setItems(FXCollections.observableArrayList(titles));

    }

    public void goBackToHome() {
        try {
            // Load the home page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
            e.printStackTrace();
        }
    }
}
