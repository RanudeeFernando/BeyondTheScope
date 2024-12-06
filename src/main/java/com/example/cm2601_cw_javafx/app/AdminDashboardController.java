package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.model.Admin;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class AdminDashboardController extends AdminBaseController{

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Text welcomeLabel;

    private Admin currentAdmin;

    // Set the current admin method
    @Override
    public void setAdmin(Admin admin) {
        this.currentAdmin = admin;

    }

    public void initialize() {
        // Update the welcome label dynamically
        Platform.runLater(() -> welcomeLabel.setText("Hello, " + currentAdmin.getUsername() + "!"));
    }

    // Navigate to Fetch Articles page
    @FXML
    private void handleFetchArticlesButton() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/fetch-articles.fxml"));
            Parent root = loader.load();

            FetchArticlesController controller = loader.getController();
            controller.setCurrentAdmin(currentAdmin);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("Error while navigating to the Fetch Articles page.");

        }
    }

    // Navigate to Update Category page
    @FXML
    private void handleUpdateArticleCategoryButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/update-category.fxml"));
            Parent root = loader.load();

            UpdateArticleCategoryController controller = loader.getController();
            controller.setAdmin(currentAdmin);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            System.out.println("Error while navigating to the Update Article Category page.");

        }
    }

    // Navigate to Delete Articles page
    @FXML
    private void handleDeleteArticles() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/delete-articles.fxml"));
            Parent root = loader.load();

            DeleteArticlesController controller = loader.getController();
            controller.setAdmin(currentAdmin);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            System.out.println("Error while navigating to the Delete Articles page.");

        }
    }

    // Log out and navigate to Main page
    @FXML
    private void handleLogout() {

        try {
            showAlert("Logout successful! You will be directed to the Main page soon.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/main.fxml"));
            Parent root = loader.load();

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("Error while navigating to the main page.");

        }
    }




}
