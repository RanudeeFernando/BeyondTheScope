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

    private UserSession userSession = UserSession.getInstance();

    private int currentArticleId;
    private UserManager userManager = new UserManager();

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
            publishedDateLabel.setText("Published on: N/A"); // Fallback if no date is available
        }

        articleURL.setOnAction(event -> {
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(article.getUrl()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    public void setLikeButtonStatus(){
        int currentUserId = userSession.getUserId();  // Get the logged-in user ID
        if (userManager.hasLikedArticle(currentUserId, currentArticleId)) {
            likeButton.setText("Liked");
        } else {
            likeButton.setText("Like");
        }
    }

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

    public void onLikeButtonClick() {
        int currentUserId = userSession.getUserId();
        boolean isLiked = userManager.hasLikedArticle(currentUserId, currentArticleId);

        if (isLiked) {
            userManager.removeLikedArticle(currentUserId, currentArticleId);
            likeButton.setText("Like");
        } else {
            // User has not liked the article, so add the like
            userManager.addLikedArticle(currentUserId, currentArticleId);
            likeButton.setText("Liked");  // Update the button text to "Liked"
        }
    }

}
