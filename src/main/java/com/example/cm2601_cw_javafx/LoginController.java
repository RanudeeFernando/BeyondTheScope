package com.example.cm2601_cw_javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class LoginController extends BaseController{

    @FXML
    private ImageView imageViewLogo;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;

//    private final RegularUserManager regularUserManager = new RegularUserManager();

//    public void initialize() {
//        setLogoImage(imageViewLogo, "images/logo5.png");
//    }

//    @FXML
//    public void handleLogin() {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//
//        // First, validate credentials using loginUser()
//        String result = regularUserManager.loginUser(username, password);
//
//        if (result.equals("Login successful!")) {
//            // Retrieve the user ID separately
//            int userId = regularUserManager.getUserIdByUsername(username);
//            if (userId != -1) { // Assuming -1 indicates user ID not found
//                // Set user information in UserSession
//                UserSession.getInstance().setUserId(userId);
//                UserSession.getInstance().setUsername(username);
//
//                // Show success alert
//                showAlert(result);
//
//                // Clear input fields
//                clearFields();
//
//                // Navigate to the home page
//                navigateToHomePage();
//
//            } else {
//                showAlert("An error occurred while retrieving user information.");
//            }
//        }
//        else {
//            showAlert(result);
//        }
//    }

//    private void showAlert(String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    private void clearFields() {
//        usernameField.clear();
//        passwordField.clear();
//    }
//
//    private void navigateToHomePage() {
//        try {
//            // Load the home page FXML file
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
//            Parent root = loader.load();
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

    // ------------------------------------------------------------------------------------------------------


    private final UserDAO userDAO = new UserDAO();
    private final SystemUserManager systemUserManager = new SystemUserManager(userDAO);


    public void initialize() {
        setLogoImage(imageViewLogo, "images/logo5.png");
    }

    // METHOD 1 AFTER ADDING ADMIN
//    @FXML
//    public void handleLogin() {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//
//        // Step 1: Authenticate the user
//        String authResult = systemUserManager.authenticateUser(username, password);
//
//        if ("Login successful!".equals(authResult)) {
//
//            showAlert(authResult);
//
//            SystemUser loggedInUser = systemUserManager.getUserByRole(username);
//
//            if (loggedInUser instanceof Admin) {
//                navigateToAdminDashboard();
//            } else if (loggedInUser instanceof RegularUser) {
//                navigateToHomePage();
//            } else {
//                showAlert("Unknown role. Login failed.");
//            }
//        }
//        else {
//            // Show the error message from authentication
//            showAlert(authResult);
//        }
//
//        // Clear input fields
//        clearFields();
//    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Step 1: Authenticate the user
        String authResult = systemUserManager.authenticateUser(username, password);

        if ("Login successful!".equals(authResult)) {
            // Retrieve the user ID separately
            int userId = systemUserManager.getUserIdByUsername(username);
            if (userId != -1) { // Assuming -1 indicates user ID not found
                // Set user information in UserSession
                UserSession.getInstance().setUserId(userId);
                UserSession.getInstance().setUsername(username);

                System.out.println("User ID set in session: " + UserSession.getInstance().getUserId());
                System.out.println("Username set in session: " + UserSession.getInstance().getUsername());

                // Retrieve the user object by role
                SystemUser loggedInUser = systemUserManager.getUserByRole(username);

                if (loggedInUser instanceof Admin) {
                    navigateToAdminDashboard();
                } else if (loggedInUser instanceof RegularUser) {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-dashboard.fxml"));
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
