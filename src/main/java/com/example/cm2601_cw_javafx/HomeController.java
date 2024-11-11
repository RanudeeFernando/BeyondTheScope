package com.example.cm2601_cw_javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.image.ImageView;
import javafx.scene.control.ListView;

import java.util.List;

public class HomeController extends BaseController{
    @FXML
    private ImageView imageViewLogo;
    @FXML
    private Menu homeMenu;
    @FXML
    private Menu profileMenu;
    @FXML
    private Menu logoutMenu;
    @FXML
    private ListView<String> articleListView; // Add ListView

    public void initialize() {
        setLogoImage(imageViewLogo, "logo5.png");
        loadArticles();
    }

    private void loadArticles() {
        List<String> articles = ArticleFetcher.fetchArticles();
        articleListView.getItems().addAll(articles);
    }





}
