package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Admin;
import com.example.cm2601_cw_javafx.model.SystemUser;
import com.example.cm2601_cw_javafx.model.User;
import com.example.cm2601_cw_javafx.service.SystemUserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController extends BaseController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    private final SystemUserManager systemUserManager = new SystemUserManager();


    // Handles login button click event
    @FXML
    public void onLoginButtonClick() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // // Authenticate the user credentials
        String authResult = systemUserManager.authenticateUser(username, password);

        if ("Login successful!".equals(authResult)) {

            // // Retrieve the user from the database
            SystemUser loggedInUser = DBManager.getUserQuery(username);

            if (loggedInUser != null) {
                // Navigate to Admin Dashboard if the user is an Admin

                if (loggedInUser instanceof Admin) {

                    showAlert("Successfully logged in as Admin! You will be redirected to the Admin Dashboard shortly.");
                    clearFields();
                    navigateToAdminDashboard((Admin) loggedInUser);


                }
                // Navigate to Home Page if the user is a regular User
                else if (loggedInUser instanceof User) {

                    showAlert("Login successful! You will be redirected to the Home page shortly.");
                    clearFields();
                    navigateToHomePage((User) loggedInUser);

                }
                else {
                    showError("Unknown role. Login failed.");
                }
            }
            else {
                showError("An error occurred while retrieving user information.");
            }
        }
        else {
            // Show authentication error messages
            showError(authResult);
        }

    }

    // Clears input fields for username and password
    @Override
    public void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }

    // Navigates to Admin dashboard
    public void navigateToAdminDashboard(Admin admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/admin-dashboard.fxml"));
            Parent root = loader.load();

            // Pass the admin user data to the AdminDashboardController
            AdminDashboardController adminController = loader.getController();
            adminController.setAdmin(admin);

            // Change the current scene to the Admin dashboard
            Scene currentScene = usernameField.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            // Log an error if navigation fails
            showAlert("An error occurred while navigating to the Admin Dashboard.");

        }

    }

    // Navigates to home page
    public void navigateToHomePage(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            // Pass the user data to the HomeController
            HomeController homeController = loader.getController();
            homeController.setCurrentUser(user);

            // Change the current scene to the Home Page
            Scene currentScene = usernameField.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            // Log an error if navigation fails
            showAlert("An error occurred while navigating to the Home page.");

        }
    }

    // Navigates back to the Main page.
    @FXML
    private void goBackToMain(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/main.fxml"));
            Parent root = loader.load();

            // Change the current scene to the Main page
            Scene scene = usernameField.getScene();
            scene.setRoot(root);

        } catch (IOException e) {
            // Log an error if navigation fails
            System.out.println("An error occurred while redirecting to Main page.");

        }
    }

}




