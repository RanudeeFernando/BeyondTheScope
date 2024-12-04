package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Article;
import com.example.cm2601_cw_javafx.model.User;

import com.example.cm2601_cw_javafx.service.SystemUserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.text.SimpleDateFormat;

public class ViewFullArticleController extends BaseController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView imageViewLogo;
    @FXML
    private Label titleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label sourceLabel;
    @FXML
    private Label publishedDateLabel;
    @FXML
    private TextArea contentTextArea;
    @FXML
    private Hyperlink articleURL;
    @FXML
    private Button likeButton;
    @FXML
    private Button skipButton;

    User user;
    private int currentArticleId;

    private final DBManager dbManager = new DBManager();
    //private final SystemUserManager systemUserManager = new SystemUserManager(dbManager);


    @Override
    public void setUser(User user) {
        this.user = user;
    }


    public void setArticleDetails(Article article) {
        this.currentArticleId = article.getArticleID();
        titleLabel.setText(article.getTitle());
        authorLabel.setText("Author: " + article.getAuthor());
        sourceLabel.setText("Source: " + article.getSource());
        contentTextArea.setText(article.getContent());

        // Format and set the published date
        if (article.getPublishedDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            publishedDateLabel.setText("Published on: " + dateFormat.format(article.getPublishedDate()));
        } else {
            publishedDateLabel.setText("Published on: N/A");
        }

        articleURL.setOnAction(event -> {
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(article.getUrl()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        setButtonStates();


    }

//    private void setButtonStates() {
//        int currentUserId = user.getUserID();
//
//
//        // Check if the article is liked or skipped
//        //boolean isLiked = systemUserManager.hasLikedArticle(currentUserId, currentArticleId);
//
//        boolean isLiked = user.hasLikedArticle(currentArticleId);
//
//        //boolean isSkipped = systemUserManager.hasSkippedArticle(currentUserId, currentArticleId);
//
//        boolean isSkipped = user.hasSkippedArticle(currentArticleId);
//
//        // Log states for debugging
//        System.out.println("isLiked: " + isLiked + ", isSkipped: " + isSkipped);
//
//        // Update button text
//        likeButton.setText(isLiked ? "Liked" : "Like");
//        skipButton.setText(isSkipped ? "Skipped" : "Skip");
//
//        // Update button states
//        skipButton.setDisable(isLiked);
//        likeButton.setDisable(isSkipped);
//    }
//
//    public void onLikeButtonClick() {
//        int currentUserId = user.getUserID();
//        //boolean isLiked = systemUserManager.hasLikedArticle(currentUserId, currentArticleId);
//        boolean isLiked = user.hasLikedArticle(currentArticleId);
//
//        if (isLiked) {
//            //systemUserManager.unlikeArticle(currentUserId, currentArticleId);
//            dbManager.removeInteraction(currentUserId, currentArticleId);
//            showAlert("Article unliked successfully! Your recommendations will be updated accordingly.");
//        } else {
//
//            //systemUserManager.likeArticle(currentUserId, currentArticleId);
//            dbManager.addInteraction(currentUserId, currentArticleId, "LIKE");
//            showAlert("Article liked successfully! Your recommendations will be updated accordingly.");
//        }
//
//        setButtonStates();
//    }
//
//    public void onSkipButtonClick() {
//        int currentUserId = user.getUserID();
//        boolean isSkipped = systemUserManager.hasSkippedArticle(currentUserId, currentArticleId);
//
//        if (!isSkipped) {
//            //systemUserManager.skipArticle(currentUserId, currentArticleId);
//            dbManager.addInteraction(currentUserId, currentArticleId, "SKIP");
//
//            showAlert("Article skipped successfully!");
//
//        }
//        else {
//            //systemUserManager.unskipArticle(currentUserId, currentArticleId);
//
//            dbManager.removeInteraction(currentUserId, currentArticleId);
//            showAlert("Article unskipped successfully!");
//        }
//
//        setButtonStates();
//    }


    // Method to return to the home view
    public void goBackToHome() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            HomeController homeController = loader.getController();
            homeController.setUser(user);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
            e.printStackTrace();
        }
    }


    // ------------------------------

    public void setButtonStates() {
        Article currentArticle = getCurrentArticle(); // Fetch the current article based on currentArticleId

        // Check if the article is liked or skipped using the User's lists
        boolean isLiked = user.hasLikedArticle(currentArticle);
        boolean isSkipped = user.hasSkippedArticle(currentArticle);

        // Log states for debugging
        System.out.println("isLiked: " + isLiked + ", isSkipped: " + isSkipped);
        System.out.println(currentArticle);

        // Update button text
        likeButton.setText(isLiked ? "Liked" : "Like");
        skipButton.setText(isSkipped ? "Skipped" : "Skip");

        // Update button states
        skipButton.setDisable(isLiked);
        likeButton.setDisable(isSkipped);
    }

    public void onLikeButtonClick() {
        Article currentArticle = getCurrentArticle(); // Fetch the current article based on currentArticleId

        if (user.hasLikedArticle(currentArticle)) {
            user.unlikeArticle(currentArticle);
            showAlert("Article unliked successfully! Your recommendations will be updated accordingly.");
        } else {
            user.likeArticle(currentArticle);
            showAlert("Article liked successfully! Your recommendations will be updated accordingly.");
        }

        setButtonStates();
    }

    public void onSkipButtonClick() {
        Article currentArticle = getCurrentArticle(); // Fetch the current article based on currentArticleId

        if (user.hasSkippedArticle(currentArticle)) {
            user.unskipArticle(currentArticle);
            showAlert("Article unskipped successfully!");
        } else {
            user.skipArticle(currentArticle);
            showAlert("Article skipped successfully!");
        }

        setButtonStates();
    }

    // Helper method to fetch the current Article object
    private Article getCurrentArticle() {
        return new Article(currentArticleId, titleLabel.getText());
    }





}
