package com.example.cm2601_cw_javafx;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.image.ImageView;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController extends BaseController{
    @FXML
    private ImageView imageViewLogo;
    @FXML
    private Menu homeMenu;
    @FXML
    private Menu profileMenu;
    @FXML
    private Menu logoutMenu;
    @FXML
    private ListView<String> articleListView; // Add ListView

    private final ArticleService articleService = new ArticleService();

    public void initialize() {
        setLogoImage(imageViewLogo, "logo5.png");
        loadArticles();

        // Set up double-click event for ListView items
        articleListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Detect double-click
                String selectedTitle = articleListView.getSelectionModel().getSelectedItem();
                if (selectedTitle != null) {
                    // Find the Article object corresponding to the selected title
                    Article selectedArticle = articleService.getAllArticles()
                            .stream()
                            .filter(article -> article.getTitle().equals(selectedTitle))
                            .findFirst()
                            .orElse(null);

                    if (selectedArticle != null) {
                        navigateToArticleDetails(selectedArticle);
                    }
                }
            }
        });
    }

    private void loadArticles() {
        List<Article> articles = articleService.getAllArticles();
        List<String> titles = articles.stream()
                .map(Article::getTitle) // Extracting the title for display
                .collect(Collectors.toList());
        articleListView.setItems(FXCollections.observableArrayList(titles));
    }

    private void navigateToArticleDetails(Article article) {
        try {
            // Load the ArticleDetails.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-full-article.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the article data
            ViewFullArticleController controller = loader.getController();
            controller.setArticleDetails(article);

            // Get the current scene and set the new root
            Scene currentScene = articleListView.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Article Details page.");
            e.printStackTrace();
        }
    }





}
