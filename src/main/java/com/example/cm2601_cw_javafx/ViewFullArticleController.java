package com.example.cm2601_cw_javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.text.SimpleDateFormat;

public class ViewFullArticleController extends BaseController{

    @FXML
    private ImageView imageViewLogo;
    @FXML
    private Menu homeMenu;
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
    private AnchorPane goHomeMenuItem;

    public void initialize() {
        setLogoImage(imageViewLogo, "logo5.png");
    }

    public void setArticleDetails(Article article) {
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


    }


    // Method to return to the home view
    public void goBackToHome() {
        try {
            // Load the home page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            // Get the current scene and set the new root
            Scene currentScene = goHomeMenuItem.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
            e.printStackTrace();
        }
    }
}
