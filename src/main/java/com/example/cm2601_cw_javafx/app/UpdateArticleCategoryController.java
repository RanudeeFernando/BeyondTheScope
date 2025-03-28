package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Admin;
import com.example.cm2601_cw_javafx.model.Article;
import com.example.cm2601_cw_javafx.model.Category;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class UpdateArticleCategoryController extends AdminBaseController{

    @FXML
    private ListView<String> articleListView;
    @FXML
    private TextField articleIdField;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private AnchorPane rootPane;


    private Admin currentAdmin;

    // Sets the current admin
    public void setAdmin(Admin currentAdmin) {
        this.currentAdmin = currentAdmin;

    }

    // Initializes the controller
    @FXML
    private void initialize() {
        loadArticles();
        for (Category category : Category.values()) {
            if (category != Category.UNKNOWN) {
                categoryComboBox.getItems().add(category);
            }
        }
    }

    // Loads all articles from the database into the ListView
    private void loadArticles() {
       List<Article> articles = DBManager.getAllArticlesQuery();

        if (articles.isEmpty()) {
            showAlert("No articles found in the database.");
        } else {
            articleListView.getItems().clear();
            for (Article article : articles) {
                articleListView.getItems().add("ID: " + article.getArticleID() + " | Title: " + article.getTitle() + " | Category: " + article.getCategory());
            }
        }
    }

    // Handles the Update Category button click
    @FXML
    private void onUpdateCategoryButtonClick() {
        try {
            int articleID = Integer.parseInt(articleIdField.getText());

            Category selectedCategory = categoryComboBox.getValue();
            if (selectedCategory == null) {
                showError("Please select a category to update.");
                return;
            }

            Article article = DBManager.getArticleByIDQuery(articleID);

            if (article == null) {
                showError("Article ID " + articleID + " not found.");
                return;
            }

            article.setCategory(selectedCategory);
            currentAdmin.updateArticleCategory(article);

            showAlert("Article ID " + articleID + " updated to category " + selectedCategory + ".");
            articleIdField.clear();
            categoryComboBox.setValue(null);
            loadArticles();

        } catch (NumberFormatException e) {
            showError("Invalid Article ID. Please enter a valid number.");
        } catch (Exception e) {
            showError("Error updating category: " + e.getMessage());
        }
    }


    // Navigates back to the admin dashboard
    @FXML
    private void goBackToDashboard() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/admin-dashboard.fxml"));
            Parent root = loader.load();

            AdminDashboardController controller = loader.getController();
            controller.setAdmin(currentAdmin);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Admin Dashboard.");
        }
    }

}
