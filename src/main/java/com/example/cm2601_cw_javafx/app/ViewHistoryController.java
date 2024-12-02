package com.example.cm2601_cw_javafx.app;


import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.User;
import com.example.cm2601_cw_javafx.model.UserViewedArticle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class ViewHistoryController extends BaseController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView imageViewLogo;
    @FXML
    private ListView<String> articleListView;

    // private HistoryService historyService;

    User user;

    @Override
    public void setUser(User user) {
        this.user = user;
    }




    public void initializeUserViewHistory(int userId) {
        Connection connection = DBManager.connectToDatabase();

        if (connection != null) {

            DBManager DBManager = new DBManager();

            List<UserViewedArticle> viewedArticles = DBManager.getViewedArticles(userId);

            ObservableList<String> listViewItems = FXCollections.observableArrayList(
                    viewedArticles.stream().map(UserViewedArticle::toListViewString).collect(Collectors.toList())
            );

            // Set the retrieved articles to the ListView
            articleListView.setItems(listViewItems);

        } else {
            System.out.println("Failed to initialize user view history due to database connection issues.");
        }
    }

    // Method to return to the home view
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
