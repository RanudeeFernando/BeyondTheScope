package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.model.Admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

    private Admin loggedInAdmin;

    public void setAdmin(Admin admin) {
        this.loggedInAdmin = admin;

    }

    @FXML
    private void handleFetchArticlesButton() {

        System.out.println("Admin " + loggedInAdmin.getUsername() + " is selected the fetch articles option.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/fetch-articles.fxml"));
            Parent root = loader.load();

            FetchArticlesController controller = loader.getController();
            controller.setAdmin(loggedInAdmin);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("Error while navigating to the Fetch Articles page.");
            e.printStackTrace();
        }
    }

//    @FXML
//    private void handleCategorizeArticles() {
//        System.out.println("Admin " + loggedInAdmin.getUsername() + " is selected the categorize article option.");
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/categorize-articles.fxml"));
//            Parent root = loader.load();
//
//            CategorizeArticlesController controller = loader.getController();
//            controller.setAdmin(loggedInAdmin);
//
//            Scene currentScene = rootPane.getScene();
//            currentScene.setRoot(root);
//        } catch (IOException e) {
//            System.out.println("Error while navigating to the Categorize Articles page.");
//            e.printStackTrace();
//        }
//    }

    @FXML
    private void handleUpdateCategory() {
        System.out.println("Admin " + loggedInAdmin.getUsername() + " is selected the update category option.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/update-category.fxml"));
            Parent root = loader.load();

            UpdateArticleCategoryController controller = loader.getController();
            controller.setAdmin(loggedInAdmin);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            System.out.println("Error while navigating to the Update Article Category page.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteArticles() {
        System.out.println("Admin " + loggedInAdmin.getUsername() + " is selected delete article option.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/delete-articles.fxml"));
            Parent root = loader.load();

            DeleteArticlesController controller = loader.getController();
            controller.setAdmin(loggedInAdmin);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            System.out.println("Error while navigating to the Delete Articles page.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {

        //SessionService.getInstance().clearSession();

        try {
            showAlert("Logout successful! You will be directed to the Main page soon.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/main.fxml"));
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
