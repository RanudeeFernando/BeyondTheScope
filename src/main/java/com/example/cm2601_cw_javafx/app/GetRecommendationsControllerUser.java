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

public class GetRecommendationsControllerUser extends UserBaseController {
    @FXML
    private ListView<String> recommendationListView;
    @FXML
    private AnchorPane rootPane;

    private User currentUser;

    // Sets the current user
    @Override
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;

    }

    // Initializes the recommended articles and displays them in the ListView
    public void initializeRecommendedArticles() {
        // Generate recommendations using the RecommendationModel
        List<RecommendedItem> recommendations = RecommendationModel.generateRecommendations();

        boolean hasRecommendations = false;
        int currentUserID = currentUser.getUserID();

        // Iterate through the recommendations and display those for the current user
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


    // Navigates back to the user's home page
    public void goBackToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            // Pass the current user to the HomeController
            HomeControllerUser controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
        }
    }
}
