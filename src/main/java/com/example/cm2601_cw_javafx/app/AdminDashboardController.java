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

    public void setAdmin(Admin admin) {
        this.currentAdmin = admin;

    }

    public void initialize() {
        Platform.runLater(() -> welcomeLabel.setText("Hello, " + currentAdmin.getUsername() + "!"));
    }

    @FXML
    private void handleFetchArticlesButton() {

        System.out.println("Admin " + currentAdmin.getUsername() + " is selected the fetch articles option.");
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

    @FXML
    private void handleUpdateArticleCategoryButton() {
        System.out.println("Admin " + currentAdmin.getUsername() + " is selected the update category option.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/update-category.fxml"));
            Parent root = loader.load();

            UpdateArticleCategoryController controller = loader.getController();
            controller.setCurrentAdmin(currentAdmin);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            System.out.println("Error while navigating to the Update Article Category page.");

        }
    }

    @FXML
    private void handleDeleteArticles() {
        System.out.println("Admin " + currentAdmin.getUsername() + " is selected delete article option.");
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
