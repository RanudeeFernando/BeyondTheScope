//package com.example.cm2601_cw_javafx.app;
//
//import com.example.cm2601_cw_javafx.db.DBManager;
//import com.example.cm2601_cw_javafx.model.Admin;
//import com.example.cm2601_cw_javafx.model.Article;
//import com.example.cm2601_cw_javafx.model.Category;
//import com.example.cm2601_cw_javafx.service.ArticleCategorizer;
//import javafx.application.Platform;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextArea;
//import javafx.scene.layout.AnchorPane;
//
//import java.io.IOException;
//import java.util.List;
//
//public class CategorizeArticlesController {
//    @FXML
//    private TextArea logArea;
//    @FXML
//    private AnchorPane rootPane;
//    @FXML
//    private Button categorizeArticlesButton;
//
//    Admin admin;
//
//    public void setAdmin(Admin admin) {
//        this.admin = admin;
//
//    }
//
//    public void startCategorization() {
//        categorizeArticlesButton.setDisable(true); // Disable the button at the start
//
//        new Thread(() -> {
//            ArticleCategorizer categorizer = new ArticleCategorizer();
//
//            DBManager DBManager = new DBManager();
//
//            List<Article> unknownArticles = DBManager.getArticlesWithUnknownCategory();
//
//            if (unknownArticles.isEmpty()) {
//                appendLog("No articles with 'UNKNOWN' category to categorize.");
//                Platform.runLater(() -> categorizeArticlesButton.setDisable(false)); // Re-enable the button
//                return;
//            }
//
//            int processedCount = 0;
//            int errorCount = 0;
//            int defaultedToUnknownCount = 0;
//
//            for (Article article : unknownArticles) {
//                try {
//                    Category predictedCategory = categorizer.classifyArticle(article.getContent());
//
//                    if (predictedCategory == Category.UNKNOWN) {
//                        appendLog("Article \"" + article.getTitle() + "\" defaulted to UNKNOWN.");
//                        defaultedToUnknownCount++;
//                    } else {
//                        appendLog("Successfully categorized: \"" + article.getTitle() + "\" as " + predictedCategory + ".");
//                        processedCount++;
//                    }
//
//                    article.setCategory(predictedCategory);
//                    DBManager.updateArticleCategoryInDatabase(article);
//
//                } catch (Exception e) {
//                    appendLog("Error processing article: \"" + article.getTitle() + "\" - " + e.getMessage());
//                    errorCount++;
//                }
//            }
//
//            appendLog("Categorization complete. Successfully categorized: " + processedCount +
//                    ", Defaulted to UNKNOWN: " + defaultedToUnknownCount + ", Errors: " + errorCount);
//
//
//            Platform.runLater(() -> categorizeArticlesButton.setDisable(false));
//
//        }).start();
//    }
//
//
//    private void appendLog(String message) {
//        Platform.runLater(() -> logArea.appendText(message + "\n"));
//    }
//
//    @FXML
//    private void goBackToDashboard() {
//        try {
//
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/admin-dashboard.fxml"));
//            Parent root = loader.load();
//
//            AdminDashboardController controller = loader.getController();
//            controller.setAdmin(admin);
//
//            Scene currentScene = rootPane.getScene();
//            currentScene.setRoot(root);
//
//        } catch (IOException e) {
//            System.out.println("An error occurred while redirecting to Admin Dashboard.");
//            e.printStackTrace();
//        }
//    }
//}
