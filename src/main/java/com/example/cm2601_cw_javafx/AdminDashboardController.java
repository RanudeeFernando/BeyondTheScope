package com.example.cm2601_cw_javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class AdminDashboardController {

    public void initialize() {

        System.out.println("Welcome, Admin: " + UserSession.getInstance().getUsername());


    }
}
