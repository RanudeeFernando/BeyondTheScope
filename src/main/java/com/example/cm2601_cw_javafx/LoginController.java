package com.example.cm2601_cw_javafx;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class LoginController extends BaseController{

    @FXML
    private ImageView imageViewLogo;

    public void initialize() {
        setLogoImage(imageViewLogo, "logo5.png");
    }


}
