package com.example.cm2601_cw_javafx.controllers;

import com.example.cm2601_cw_javafx.ArticleService;
import com.example.cm2601_cw_javafx.RecommendationModel;
import com.example.cm2601_cw_javafx.RegularUser;
import com.example.cm2601_cw_javafx.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import net.librec.recommender.item.RecommendedItem;

import java.io.IOException;
import java.util.List;

public class GetRecommendationsController {
    @FXML
    private ListView<String> recommendationListView;
    @FXML
    private AnchorPane rootPane;

    RegularUser user = (RegularUser) UserSession.getInstance().getLoggedInUser();
    private int loggedInUserId = user.getUserID();


    public void setLoggedInUserId(int userId) {
        this.loggedInUserId = userId;
    }

    public void initialize() {
        List<RecommendedItem> recommendations = RecommendationModel.generateRecommendations();

        for (RecommendedItem recommendation : recommendations) {
            if (recommendation.getUserId().equals(String.valueOf(loggedInUserId))) {
                String articleName = ArticleService.getArticleNameById(recommendation.getItemId());

                String displayText = "Article: " + articleName +
                        ", Score: " + recommendation.getValue();

                recommendationListView.getItems().add(displayText);
            }
        }
    }

    public void goBackToHome() {
        try {
            // Load the home page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
            e.printStackTrace();
        }
    }
}
