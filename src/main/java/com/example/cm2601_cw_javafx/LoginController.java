package com.example.cm2601_cw_javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController extends BaseController{

    private final UserManager userManager = new UserManager();

    @FXML
    private ImageView imageViewLogo;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;

    public void initialize() {
        setLogoImage(imageViewLogo, "images/logo5.png");
    }

/*
    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String result = userManager.loginUser(username, password);

        showAlert(result);

        if (result.equals("Login successful!")) {
            clearFields();
            navigateToHomePage();
        }
    }
*/

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // First, validate credentials using loginUser()
        String result = userManager.loginUser(username, password);

        if (result.equals("Login successful!")) {
            // Retrieve the user ID separately
            int userId = userManager.getUserIdByUsername(username);
            if (userId != -1) { // Assuming -1 indicates user ID not found
                // Set user information in UserSession
                UserSession.getInstance().setUserId(userId);
                UserSession.getInstance().setUsername(username);

                // Show success alert
                showAlert(result);

                // Clear input fields
                clearFields();

                // Navigate to the home page
                navigateToHomePage();

            } else {
                // Handle error if user ID couldn't be retrieved
                showAlert("An error occurred while retrieving user information.");
            }
        }
        else {
            // Show error alert for incorrect credentials
            showAlert(result);
        }
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

            // Update the existing scene's root
            Scene scene = loginButton.getScene();
            scene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
            e.printStackTrace();

        }
    }



}
