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

    User currentUser;

    @Override
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;

    }

    public void initializeRecommendedArticles() {
        List<RecommendedItem> recommendations = RecommendationModel.generateRecommendations();

        boolean hasRecommendations = false;
        int currentUserID = currentUser.getUserID();

        for (RecommendedItem recommendation : recommendations) {
            if (recommendation.getUserId().equals(String.valueOf(currentUserID))) {
                hasRecommendations = true;

                String articleName = DBManager.getArticleNameByIdQuery(recommendation.getItemId());

                String displayText = "Article: " + articleName + ", Score: " + recommendation.getValue();

                recommendationListView.getItems().add(displayText);
            }
        }

        if (!hasRecommendations) {
            Platform.runLater(() -> showAlert("We couldn't generate recommendations for you. Please interact with articles so we can understand your preferences better!"));

        }
    }




    public void goBackToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            HomeController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
            e.printStackTrace();
        }
    }
}
