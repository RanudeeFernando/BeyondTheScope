package com.example.cm2601_cw_javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button fetchArticlesButton;
    @FXML
    private Button deleteArticlesButton;
    @FXML
    private Button handleArticleCategoryButton;
    @FXML
    private Button logoutButton;


    public void initialize() {
        System.out.println("Welcome, Admin: " + UserSession.getInstance().getLoggedInUser().getUsername());

    }

    @FXML
    private void handleFetchArticlesButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fetch-articles.fxml"));
            Parent root = loader.load();
            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("Error while navigating to the Fetch Articles page.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCategorizeArticles() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("categorize-articles.fxml"));
            Parent root = loader.load();
            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            System.out.println("Error while navigating to the Categorize Articles page.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateCategory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("update-category.fxml"));
            Parent root = loader.load();
            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            System.out.println("Error while navigating to the Update Article Category page.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteArticles() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("delete-articles.fxml"));
            Parent root = loader.load();
            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            System.out.println("Error while navigating to the Delete Articles page.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        // Clear the session
        UserSession.getInstance().clearSession();

        try {
            showAlert("Logout successful! You will be directed to the Main page soon.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            Parent root = loader.load();

            // Set the new root for the current scene
            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("Error while navigating to the main page.");
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }







}
