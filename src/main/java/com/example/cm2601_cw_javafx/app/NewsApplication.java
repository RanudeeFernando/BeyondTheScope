package com.example.cm2601_cw_javafx.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class NewsApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // Loading the main FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(NewsApplication.class.getResource("/com/example/cm2601_cw_javafx/fxml/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1100,750);

        // Set application logo
        Image icon = new Image(Objects.requireNonNull(NewsApplication.class.getResourceAsStream("/com/example/cm2601_cw_javafx/images/logo1.png")));
        stage.getIcons().add(icon);

        // Setting the stage title and scene
        stage.setTitle("News Recommendation System");
        stage.setScene(scene);

        // Disable resizing of the stage
        stage.setResizable(false);

        // Displaying the stage
        stage.show();

        // Start the background article fetch scheduler
//        startArticleFetchScheduler();

    }

//    private static void startArticleFetchScheduler() {
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//        // Schedule the fetch task to run every 6 hours
//        scheduler.scheduleAtFixedRate(() -> {
//            List<Article> articles = ArticleFetcher.fetchArticles();
//            articles.forEach(System.out::println);
//        }, 0, 6, TimeUnit.HOURS);
//
//        System.out.println("Article fetch scheduler started.");
//    }

    public static void main(String[] args) {
        launch();
    }
}