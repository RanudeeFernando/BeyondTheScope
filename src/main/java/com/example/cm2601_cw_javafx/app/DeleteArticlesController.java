package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Admin;
import com.example.cm2601_cw_javafx.model.Article;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class DeleteArticlesController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ListView<String> articleListView;
    @FXML
    private TextField articleIDTextField;


    Admin admin;

    public void setAdmin(Admin admin) {
        this.admin = admin;

    }

    public void initialize() {
        loadArticles();
    }

    @FXML
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
            e.printStackTrace();
            showAlert("Error retrieving articles from the database.");
        }
    }

//    @FXML
//    private void deleteArticle() {
//        try {
//            String articleIDText = articleIDTextField.getText().trim();
//
//            if (articleIDText.isEmpty()) {
//                showAlert("Please enter a valid Article ID.");
//                return;
//            }
//
//            //Admin admin = (Admin) SessionService.getInstance().getLoggedInUser();
//
//            boolean isDeleted = admin.deleteArticle(articleIDText);
//
//            if (isDeleted) {
//                showAlert("Article with ID " + articleIDText + " has been successfully deleted.");
//                loadArticles();
//                articleIDTextField.clear();
//            } else {
//                showAlert("No article found with the given ID: " + articleIDText);
//                articleIDTextField.clear();
//            }
//
//        } catch (NumberFormatException e) {
//            showAlert("Invalid Article ID. Please enter a numeric value.");
//
//        } catch (IllegalArgumentException e) {
//            showAlert(e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            showAlert("An error occurred while deleting the article. Please try again later.");
//        }
//    }

    @FXML
    private void onDeleteArticleButtonClick() {
        try {
            String articleIDText = articleIDTextField.getText().trim();

            if (articleIDText.isEmpty()) {
                showAlert("Please enter a valid Article ID.");
                return;
            }

            int articleID = Integer.parseInt(articleIDText);

            // Fetch the Article object based on the articleID
            Article article = DBManager.getArticleByIDQuery(articleID); // Assume this method fetches an Article by ID.

            if (article == null) {
                showAlert("No article found with the given ID: " + articleIDText);
                articleIDTextField.clear();
                return;
            }

            // Admin admin = (Admin) SessionService.getInstance().getLoggedInUser();
            boolean isDeleted = admin.deleteArticle(article); // Use the updated deleteArticle method.

            if (isDeleted) {
                showAlert("Article with ID: " + article.getArticleID() + " has been successfully deleted.");
                loadArticles(); // Refresh the list of articles.
                articleIDTextField.clear();
            } else {
                showAlert("Failed to delete the article. Please try again.");
            }

        } catch (NumberFormatException e) {
            showAlert("Invalid Article ID. Please enter a numeric value.");
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage());
        } catch (Exception e) {
            showAlert("An error occurred while deleting the article. Please try again later.");
        }
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

            AdminDashboardController controller = loader.getController();
            controller.setAdmin(admin);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Admin Dashboard.");
            e.printStackTrace();
        }
    }

}
