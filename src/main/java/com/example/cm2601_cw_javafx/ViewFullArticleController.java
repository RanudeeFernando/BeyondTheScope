package com.example.cm2601_cw_javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.text.SimpleDateFormat;

public class ViewFullArticleController extends BaseController{

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

    private final UserSession userSession = UserSession.getInstance();

    private int currentArticleId;
    private final UserManager userManager = new UserManager();

    public void initialize() {
        setLogoImage(imageViewLogo, "images/logo5.png");

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

//    public void setLikeButtonStatus(){
//        int currentUserId = userSession.getUserId();
//        if (userManager.hasLikedArticle(currentUserId, currentArticleId)) {
//            likeButton.setText("Liked");
//        } else {
//            likeButton.setText("Like");
//        }
//    }

    // Method to return to the home view
    public void goBackToHome() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
            e.printStackTrace();
        }
    }

    private void setButtonStates() {
        int currentUserId = userSession.getUserId();

        // Check if the article is liked or skipped
        boolean isLiked = userManager.hasLikedArticle(currentUserId, currentArticleId);
        boolean isSkipped = userManager.hasSkippedArticle(currentUserId, currentArticleId);

        // Log states for debugging
        System.out.println("isLiked: " + isLiked + ", isSkipped: " + isSkipped);

        // Update button text
        likeButton.setText(isLiked ? "Liked" : "Like");
        skipButton.setText(isSkipped ? "Skipped" : "Skip");

        // Update button states
        skipButton.setDisable(isLiked);
        likeButton.setDisable(isSkipped);
    }

    public void onLikeButtonClick() {
        int currentUserId = userSession.getUserId();
        boolean isLiked = userManager.hasLikedArticle(currentUserId, currentArticleId);

        if (isLiked) {
            userManager.unlikedArticle(currentUserId, currentArticleId);
            // likeButton.setText("Like");
            showAlert("Article unliked successfully! Your recommendations will be updated accordingly.");
        } else {

            userManager.likedArticle(currentUserId, currentArticleId);
            // likeButton.setText("Liked");
            showAlert("Article liked successfully! Your recommendations will be updated accordingly.");
        }

        setButtonStates();
    }

    public void onSkipButtonClick() {
        int currentUserId = userSession.getUserId();
        boolean isSkipped = userManager.hasSkippedArticle(currentUserId, currentArticleId);

        if (!isSkipped) {
            userManager.skipArticle(currentUserId, currentArticleId);
            showAlert("Article skipped successfully! You'll be redirected to the home page shortly.");
            goBackToHome();
        }
        else {
            userManager.unskipArticle(currentUserId, currentArticleId);
            showAlert("Article unskipped successfully!");
        }

        setButtonStates();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
