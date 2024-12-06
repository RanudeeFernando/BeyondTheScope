package com.example.cm2601_cw_javafx.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainController extends UserBaseController {
    @FXML
    public Button signUpButton;


    // Navigates to the Sign-Up page
    public void goToSignupPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/sign-up.fxml"));
            Parent root = loader.load();

            Scene scene = signUpButton.getScene();
            scene.setRoot(root);

        }

        catch (IOException e) {
            String errorMessage = "An error occurred while signing up.";
            System.out.println(errorMessage);
        }
    }//

    // Navigates to the Login page
    public void goToLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/login.fxml"));
            Parent root = loader.load();

            Scene scene = signUpButton.getScene();
            scene.setRoot(root);

        }

        catch (IOException e) {
            String errorMessage = "An error occurred while logging in.";
            System.out.println(errorMessage);
        }
    }
}