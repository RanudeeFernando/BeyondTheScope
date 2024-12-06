package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Admin;
import com.example.cm2601_cw_javafx.model.Article;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class DeleteArticlesController extends AdminBaseController{
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ListView<String> articleListView;
    @FXML
    private TextField articleIDTextField;


    Admin currentAdmin;

    // Sets the current admin for this controller
    @Override
    public void setAdmin(Admin admin) {
        this.currentAdmin = admin;

    }

    // Initializes the controller by loading articles into the ListView
    public void initialize() {
        loadArticles();
    }

    // Loads all articles from the database into the ListView
    private void loadArticles() {
        try {

            articleListView.getItems().clear();
            List<Article> articles = DBManager.getAllArticlesQuery();

            if (articles.isEmpty()) {
                showAlert("No articles found in the database.");
            } else {
                for (Article article : articles) {
                    articleListView.getItems().add("ID: " + article.getArticleID() + " | Title: " + article.getTitle());
                }
            }
        } catch (Exception e) {
            showAlert("Error retrieving articles from the database.");
        }
    }

    // Handles the delete article button click
    @FXML
    private void onDeleteArticleButtonClick() {
        try {
            String articleIDText = articleIDTextField.getText().trim();

            if (articleIDText.isEmpty()) {
                showError("Please enter a valid Article ID.");
                return;
            }

            int articleID = Integer.parseInt(articleIDText);

            // Fetch the Article object based on the articleID
            Article article = DBManager.getArticleByIDQuery(articleID);

            if (article == null) {
                showError("No article found with the given ID: " + articleIDText);
                articleIDTextField.clear();
                return;
            }

            boolean isDeleted = currentAdmin.deleteArticle(article);

            if (isDeleted) {
                showAlert("Article with ID: " + article.getArticleID() + " has been successfully deleted.");
                // Refresh the list of articles
                loadArticles();
                articleIDTextField.clear();

            } else {
                showError("Failed to delete the article. Please try again.");
            }

        } catch (NumberFormatException e) {
            showError("Invalid Article ID. Please enter a numeric value.");
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage());
        } catch (Exception e) {
            showAlert("An error occurred while deleting the article. Please try again later.");
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
