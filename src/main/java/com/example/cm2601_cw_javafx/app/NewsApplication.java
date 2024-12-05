package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Article;
import com.example.cm2601_cw_javafx.service.ArticleCategorizer;
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
    private static ScheduledExecutorService fetchScheduler;
    private static ScheduledExecutorService categorizeScheduler;

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

        startArticleFetchScheduler();
        startCategorizationScheduler();

    }

    private static void startArticleFetchScheduler() {
        fetchScheduler = Executors.newScheduledThreadPool(1);

        fetchScheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("\nFetching new articles...");
                List<Article> fetchedArticles = ArticleFetcher.fetchArticles();

                if (fetchedArticles.isEmpty()) {
                    System.out.println("No new articles were fetched.");
                } else {
                    fetchedArticles.forEach(System.out::println);

                    //ArticleFetcher.saveArticles(fetchedArticles);

                    for (Article article : fetchedArticles) {
                        try {
                            DBManager.insertArticleQuery(article);
                        } catch (Exception e) {
                            System.out.println("Failed to save article: " + article.getTitle());
                            e.printStackTrace();
                        }
                    }

                    System.out.println("\nFetched and saved new articles.");
                }

            } catch (Exception e) {
                System.out.println("\nError during article fetching: " + e.getMessage());

            }
        }, 0, 6, TimeUnit.HOURS); // Fetch every 6 hours
    }

    private static void startCategorizationScheduler() {
        categorizeScheduler = Executors.newScheduledThreadPool(1);

        categorizeScheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("\nStarting categorization...");
                ArticleCategorizer categorizer = new ArticleCategorizer();
                categorizer.categorizeUnknownArticles();
                System.out.println("\nCategorization complete.");
            } catch (Exception e) {
                System.out.println("\nError during article categorization: " + e.getMessage());

            }
        }, 15, 60 * 60, TimeUnit.SECONDS); // Categorize every hour
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        if (fetchScheduler != null && !fetchScheduler.isShutdown()) {
            fetchScheduler.shutdownNow();
        }

        if (categorizeScheduler != null && !categorizeScheduler.isShutdown()) {
            categorizeScheduler.shutdownNow();
        }

        DBManager.shutdownExecutor();

        System.out.println("\nApplication stopped, and schedulers shut down.");

    }


    public static void main(String[] args) {
        launch();
    }
}