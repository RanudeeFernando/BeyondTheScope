package com.example.cm2601_cw_javafx.app;

import javafx.scene.control.Alert;

public class AdminBaseController {
    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
