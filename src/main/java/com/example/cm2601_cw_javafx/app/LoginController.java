package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.UserDBManager;
import com.example.cm2601_cw_javafx.model.Admin;
import com.example.cm2601_cw_javafx.model.SystemUser;
import com.example.cm2601_cw_javafx.model.User;
import com.example.cm2601_cw_javafx.model.UserSession;
import com.example.cm2601_cw_javafx.service.SystemUserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController extends BaseController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;

    private final UserDBManager userDBManager = new UserDBManager();
    private final SystemUserManager systemUserManager = new SystemUserManager(userDBManager);


    public void initialize() {

    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String authResult = systemUserManager.authenticateUser(username, password);

        if ("Login successful!".equals(authResult)) {
            SystemUser loggedInUser = systemUserManager.getUserByRole(username);

            if (loggedInUser != null) {
                UserSession.getInstance().setLoggedInUser(loggedInUser);

                System.out.println("User set in session: " + loggedInUser.getUsername());

                if (loggedInUser instanceof Admin) {
                    showAlert("Successfully logged in as Admin! You will be redirected to the Admin Dashboard shortly.");
                    navigateToAdminDashboard();
                } else if (loggedInUser instanceof User) {
                    showAlert("Login successful! You will be redirected to the Home page shortly.");
                    navigateToHomePage();
                } else {
                    showAlert("Unknown role. Login failed.");
                }
            } else {
                showAlert("An error occurred while retrieving user information.");
            }
        } else {
            showAlert(authResult);
        }

        clearFields();
    }



    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }

    private void navigateToHomePage() {
        try {
            // Load the home page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();


            Scene scene = loginButton.getScene();
            scene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
            e.printStackTrace();

        }
    }

    private void navigateToAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/admin-dashboard.fxml"));
            Parent root = loader.load();

            Scene scene = loginButton.getScene();
            scene.setRoot(root);

        }
        catch (IOException e) {
            System.out.println("Error navigating to Admin Dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }



}
