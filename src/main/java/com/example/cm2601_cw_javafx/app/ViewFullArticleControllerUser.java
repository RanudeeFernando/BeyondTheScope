package com.example.cm2601_cw_javafx.app;


import com.example.cm2601_cw_javafx.model.Article;
import com.example.cm2601_cw_javafx.model.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.text.SimpleDateFormat;

public class ViewFullArticleControllerUser extends UserBaseController {

    @FXML
    private AnchorPane rootPane;
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

    private User user;
    private int currentArticleId;


    // Sets the current user
    @Override
    public void setCurrentUser(User currentUser) {
        this.user = currentUser;
    }

    // Populates article details in the view
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

        // Set the action for the article URL hyperlink
        articleURL.setOnAction(event -> {
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(article.getUrl()));
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        });

        // Update button states
        setButtonStates();


    }


    // Method to return to the home page
    public void goBackToHome() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            HomeControllerUser homeController = loader.getController();
            homeController.setCurrentUser(user);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");

        }
    }

    // Updates the states of the Like and Skip buttons
    public void setButtonStates() {
        Article currentArticle = getCurrentArticle();

        // Check if the article is liked or skipped using the User's lists
        boolean isLiked = user.hasLikedArticle(currentArticle);
        boolean isSkipped = user.hasSkippedArticle(currentArticle);


        // Update button text
        likeButton.setText(isLiked ? "Liked" : "Like");
        skipButton.setText(isSkipped ? "Skipped" : "Skip");

        // Update button states
        skipButton.setDisable(isLiked);
        likeButton.setDisable(isSkipped);
    }

    // Handles the Like button click
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

    // Handles the Skip button click
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

    // Fetch the current Article object
    private Article getCurrentArticle() {
        return new Article(currentArticleId, titleLabel.getText());
    }





}
