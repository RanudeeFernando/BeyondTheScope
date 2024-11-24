package com.example.cm2601_cw_javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class WelcomePageController extends BaseController{
    @FXML
    public Button signUpButton;

    @FXML
    private ImageView imageViewLogo;

    public void initialize() {

    }

    public void onSignUpButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sign-up.fxml"));
            Parent root = loader.load();

            Scene scene = signUpButton.getScene();
            scene.setRoot(root);

        }

        catch (IOException e) {
            String errorMessage = "An error occurred while signing up.";
            System.out.println(errorMessage);
        }
    }

    public void onLoginButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
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