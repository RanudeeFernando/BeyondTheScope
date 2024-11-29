package com.example.cm2601_cw_javafx.controllers;

import com.example.cm2601_cw_javafx.*;
import com.example.cm2601_cw_javafx.controllers.BaseController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class LoginController extends BaseController {

    @FXML
    private ImageView imageViewLogo;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;

    private final UserDAO userDAO = new UserDAO();
    private final SystemUserManager systemUserManager = new SystemUserManager(userDAO);


    public void initialize() {

    }


//    @FXML
//    public void handleLogin() {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//
//        String authResult = systemUserManager.authenticateUser(username, password);
//
//        if ("Login successful!".equals(authResult)) {
//
//            int userId = systemUserManager.getUserIdByUsername(username);
//            if (userId != -1) {
//
//                UserSession.getInstance().setUserId(userId);
//                UserSession.getInstance().setUsername(username);
//
//                System.out.println("User ID set in session: " + UserSession.getInstance().getUserId());
//                System.out.println("Username set in session: " + UserSession.getInstance().getUsername());
//
//                SystemUser loggedInUser = UserSession.getInstance().getLoggedInUser();
//
//                // SystemUser loggedInUser = systemUserManager.getUserByRole(username);
//
//                if (loggedInUser instanceof Admin) {
//                    navigateToAdminDashboard();
//                } else if (loggedInUser instanceof RegularUser) {
//                    navigateToHomePage();
//                } else {
//                    showAlert("Unknown role. Login failed.");
//                }
//            } else {
//                showAlert("An error occurred while retrieving user information.");
//            }
//        } else {
//            showAlert(authResult);
//        }
//
//        clearFields();
//    }

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
                } else if (loggedInUser instanceof RegularUser) {
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
