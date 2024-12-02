package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.model.Article;
import com.example.cm2601_cw_javafx.service.ArticleFetcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NewsApplication extends Application {
    private static ScheduledExecutorService scheduler;
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
        startArticleFetchScheduler();

    }

    private static void startArticleFetchScheduler() {
        scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            List<Article> articles = ArticleFetcher.fetchArticles();
            ArticleFetcher.saveArticles(articles);
            articles.forEach(System.out::println);
        }, 0, 6, TimeUnit.HOURS);

        System.out.println("Article fetch scheduler started.");
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.out.println("Forcing scheduler shutdown...");
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                System.out.println("Scheduler shutdown interrupted. Forcing shutdown...");
                scheduler.shutdownNow();
            }
        }

        System.out.println("Application stopped and scheduler shut down.");
    }

    public static void main(String[] args) {
        launch();
    }
}