package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.model.Article;
//import com.example.cm2601_cw_javafx.ArticleService;
import com.example.cm2601_cw_javafx.model.Category;
import com.example.cm2601_cw_javafx.db.UserDBManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class UpdateArticleCategoryController {

    @FXML
    private ListView<String> articleListView;
    @FXML
    private TextField articleIdField;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextArea logArea;

    UserDBManager userDBManager = new UserDBManager();

    @FXML
    private void initialize() {
        loadArticles();
        for (Category category : Category.values()) {
            if (category != Category.UNKNOWN) {
                categoryComboBox.getItems().add(category);
            }
        }
    }

    private void loadArticles() {
        // ArticleService articleService = new ArticleService();

        List<Article> articles = userDBManager.getAllArticles();

        articleListView.getItems().clear();
        for (Article article : articles) {
            articleListView.getItems().add("ID: " + article.getArticleID() + " | Title: " + article.getTitle() + " | Category: " + article.getCategory());
        }
    }

    @FXML
    private void handleUpdateCategory() {
        try {
            int articleID = Integer.parseInt(articleIdField.getText());

            Category selectedCategory = categoryComboBox.getValue();
            if (selectedCategory == null) {
                showAlert("Please select a category to update.");
                return;
            }

            //ArticleService articleService = new ArticleService();

            Article article = userDBManager.getArticleByID(articleID);

            if (article == null) {
                showAlert("Article ID " + articleID + " not found.");
                return;
            }

            article.setCategory(selectedCategory);
            userDBManager.updateArticleCategoryInDatabase(article);

            showAlert("Article ID " + articleID + " updated to category " + selectedCategory + ".");
            articleIdField.clear();
            categoryComboBox.setValue(null);
            loadArticles();

        } catch (NumberFormatException e) {
            showAlert("Invalid Article ID. Please enter a valid number.");
        } catch (Exception e) {
            showAlert("Error updating category: " + e.getMessage());
        }
    }

    private void appendLog(String message) {
        logArea.appendText(message + "\n");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goBackToDashboard() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/admin-dashboard.fxml"));
            Parent root = loader.load();

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Admin Dashboard.");
            e.printStackTrace();
        }
    }

}
