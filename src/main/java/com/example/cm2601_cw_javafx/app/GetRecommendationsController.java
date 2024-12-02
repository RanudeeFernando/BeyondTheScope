package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.User;

import com.example.cm2601_cw_javafx.service.RecommendationModel;
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

    //User user = (User) SessionService.getInstance().getLoggedInUser();
    //private int loggedInUserId = user.getUserID();

    User user;


//    public void setLoggedInUserId(int userId) {
//        this.loggedInUserId = userId;
//    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

//    public void initialize() {
//        List<RecommendedItem> recommendations = RecommendationModel.generateRecommendations();
//
//        for (RecommendedItem recommendation : recommendations) {
//            if (recommendation.getUserId().equals(String.valueOf(loggedInUserId))) {
//                //String articleName = ArticleService.getArticleNameById(recommendation.getItemId());
//
//                String articleName = DBManager.getArticleNameById(recommendation.getItemId());
//
//                String displayText = "Article: " + articleName +
//                        ", Score: " + recommendation.getValue();
//
//                System.out.println("Article id: " + recommendation.getItemId());
//
//                recommendationListView.getItems().add(displayText);
//            }
//        }
//    }

    public void initializeRecommendedArticles() {
        List<RecommendedItem> recommendations = RecommendationModel.generateRecommendations();

        for (RecommendedItem recommendation : recommendations) {
            if (recommendation.getUserId().equals(String.valueOf(user.getUserID()))) {

                //String articleName = ArticleService.getArticleNameById(recommendation.getItemId());

                String articleName = DBManager.getArticleNameById(recommendation.getItemId());

                String displayText = "Article: " + articleName +
                        ", Score: " + recommendation.getValue();

                System.out.println("Article id: " + recommendation.getItemId());

                recommendationListView.getItems().add(displayText);
            }
        }
    }


    public void goBackToHome() {
        try {
            // Load the home page FXML file
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
