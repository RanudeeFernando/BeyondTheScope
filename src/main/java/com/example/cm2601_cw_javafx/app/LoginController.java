package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Admin;
import com.example.cm2601_cw_javafx.model.Category;
import com.example.cm2601_cw_javafx.model.SystemUser;
import com.example.cm2601_cw_javafx.model.User;

import com.example.cm2601_cw_javafx.service.SystemUserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class LoginController extends BaseController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;

    private final SystemUserManager systemUserManager = new SystemUserManager();


//    @FXML
//    public void handleLogin() {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//
//        String authResult = systemUserManager.authenticateUser(username, password);
//
//        if ("Login successful!".equals(authResult)) {
//            SystemUser loggedInUser = systemUserManager.getUser(username);
//
//            if (loggedInUser != null) {
//                SessionService.getInstance().setLoggedInUser(loggedInUser);
//
//                System.out.println("User set in session: " + loggedInUser.getUsername());
//
//                if (loggedInUser instanceof Admin) {
//                    showAlert("Successfully logged in as Admin! You will be redirected to the Admin Dashboard shortly.");
//                    navigateToAdminDashboard();
//                } else if (loggedInUser instanceof User) {
//                    showAlert("Login successful! You will be redirected to the Home page shortly.");
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



    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }

//    private void navigateToHomePage() {
//        try {
//            // Load the home page FXML file
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
//            Parent root = loader.load();
//
//
//            Scene scene = loginButton.getScene();
//            scene.setRoot(root);
//
//        } catch (IOException e) {
//            System.out.println("An error occurred while redirecting to Home page.");
//            e.printStackTrace();
//
//        }
//    }
//
//    private void navigateToAdminDashboard() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/admin-dashboard.fxml"));
//            Parent root = loader.load();
//
//            Scene scene = loginButton.getScene();
//            scene.setRoot(root);
//
//        }
//        catch (IOException e) {
//            System.out.println("Error navigating to Admin Dashboard: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    @FXML
    public void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String authResult = systemUserManager.authenticateUser(username, password);

        if ("Login successful!".equals(authResult)) {
            SystemUser loggedInUser = systemUserManager.getUser(username);

            if (loggedInUser != null) {
                System.out.println("User authenticated: " + loggedInUser.getUsername());

                if (loggedInUser instanceof Admin) {

                    showAlert("Successfully logged in as Admin! You will be redirected to the Admin Dashboard shortly.");
                    navigateToAdminDashboard((Admin) loggedInUser);

                } else if (loggedInUser instanceof User) {

                    showAlert("Login successful! You will be redirected to the Home page shortly.");
                    navigateToHomePage((User) loggedInUser);

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

    private void navigateToAdminDashboard(Admin admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/admin-dashboard.fxml"));
            Parent root = loader.load();

            AdminDashboardController adminController = loader.getController();
            adminController.setAdmin(admin);

            Scene currentScene = usernameField.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            showAlert("An error occurred while navigating to the Admin Dashboard.");
        }
    }

    private void navigateToHomePage(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            HomeController homeController = loader.getController();
            homeController.setUser(user);

            Scene currentScene = usernameField.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            showAlert("An error occurred while navigating to the Home page.");
        }
    }

}




