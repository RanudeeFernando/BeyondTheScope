package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.User;
import com.example.cm2601_cw_javafx.service.RecommendationModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import net.librec.recommender.item.RecommendedItem;

import java.io.IOException;
import java.util.List;

public class GetRecommendationsController extends BaseController{
    @FXML
    private ListView<String> recommendationListView;
    @FXML
    private AnchorPane rootPane;

    User user;

    @Override
    public void setUser(User user) {
        this.user = user;

    }

    public void initializeRecommendedArticles() {
        List<RecommendedItem> recommendations = RecommendationModel.generateRecommendations();

        boolean hasRecommendations = false;

        for (RecommendedItem recommendation : recommendations) {
            if (recommendation.getUserId().equals(String.valueOf(user.getUserID()))) {
                hasRecommendations = true;

                String articleName = DBManager.getArticleNameById(recommendation.getItemId());

                String displayText = "Article: " + articleName + ", Score: " + recommendation.getValue();

                recommendationListView.getItems().add(displayText);
            }
        }

        if (!hasRecommendations) {
            showAlert("We couldn't generate recommendations for you. Please interact with articles so we can understand your preferences better!");

        }
    }




    public void goBackToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            HomeController controller = loader.getController();
            controller.setUser(user);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
            e.printStackTrace();
        }
    }
}
