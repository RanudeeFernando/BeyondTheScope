package com.example.cm2601_cw_javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Objects;

public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    private ImageView imageViewLogo;


    public void initialize() {
        // Loading the image
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo2.png")));
        // Setting the image to the ImageView
        imageViewLogo.setImage(image);
    }

    @FXML
    protected void onSignUpButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sign-up.fxml"));
            Parent root = loader.load();



        }

        catch (IOException e) {
            e.printStackTrace();
            String errorMessage = "An error occurred while handling the add horse details button";
            System.out.println(errorMessage);
        }

    }
}