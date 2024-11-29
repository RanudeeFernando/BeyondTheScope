package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.*;
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

    // private final UserSession userSession = UserSession.getInstance();
    User user = (User) UserSession.getInstance().getLoggedInUser();

    private int currentArticleId;

    // private final RegularUserManager regularUserManager = new RegularUserManager();

    private final UserDAO userDAO = new UserDAO();
    private final SystemUserManager systemUserManager = new SystemUserManager(userDAO);

    public void initialize() {


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


    // Method to return to the home view
    public void goBackToHome() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
            e.printStackTrace();
        }
    }

    private void setButtonStates() {
        int currentUserId = user.getUserID();

        // Check if the article is liked or skipped
        boolean isLiked = systemUserManager.hasLikedArticle(currentUserId, currentArticleId);
        boolean isSkipped = systemUserManager.hasSkippedArticle(currentUserId, currentArticleId);

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
        int currentUserId = user.getUserID();
        boolean isLiked = systemUserManager.hasLikedArticle(currentUserId, currentArticleId);

        if (isLiked) {
            systemUserManager.unlikeArticle(currentUserId, currentArticleId);
            // likeButton.setText("Like");
            showAlert("Article unliked successfully! Your recommendations will be updated accordingly.");
        } else {

            systemUserManager.likeArticle(currentUserId, currentArticleId);
            // likeButton.setText("Liked");
            showAlert("Article liked successfully! Your recommendations will be updated accordingly.");
        }

        setButtonStates();
    }

    public void onSkipButtonClick() {
        int currentUserId = user.getUserID();
        boolean isSkipped = systemUserManager.hasSkippedArticle(currentUserId, currentArticleId);

        if (!isSkipped) {
            systemUserManager.skipArticle(currentUserId, currentArticleId);
            showAlert("Article skipped successfully! You'll be redirected to the home page shortly.");
            goBackToHome();
        }
        else {
            systemUserManager.unskipArticle(currentUserId, currentArticleId);
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
