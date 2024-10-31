package com.example.cm2601_cw_javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class NewsRecommendationApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Loading the main FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(NewsRecommendationApplication.class.getResource("main-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1100,700);

        // Set application logo
        Image icon = new Image(Objects.requireNonNull(NewsRecommendationApplication.class.getResourceAsStream("logo1.png")));
        stage.getIcons().add(icon);

        // Setting the stage title and scene
        stage.setTitle("New Recommendation System");
        stage.setScene(scene);

        // Disable resizing of the stage
        stage.setResizable(false);

        // Displaying the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}