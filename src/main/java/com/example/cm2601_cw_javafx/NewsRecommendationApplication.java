package com.example.cm2601_cw_javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NewsRecommendationApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // Loading the main FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(NewsRecommendationApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1100,750);

        // Set application logo
        Image icon = new Image(Objects.requireNonNull(NewsRecommendationApplication.class.getResourceAsStream("logo1.png")));
        stage.getIcons().add(icon);

        // Setting the stage title and scene
        stage.setTitle("News Recommendation System");
        stage.setScene(scene);

        // Disable resizing of the stage
        stage.setResizable(false);

        // Displaying the stage
        stage.show();

        // Start the background article fetch scheduler
        startArticleFetchScheduler();

    }

    private static void startArticleFetchScheduler() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule the fetch task to run every 6 hours
        scheduler.scheduleAtFixedRate(() -> {
            List<String> articles = ArticleFetcher.fetchArticles();
            articles.forEach(System.out::println);
        }, 0, 6, TimeUnit.HOURS);

        System.out.println("Article fetch scheduler started.");
    }

    public static void main(String[] args) {
        launch();
    }
}