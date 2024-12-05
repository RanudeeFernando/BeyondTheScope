package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.model.User;
import javafx.scene.control.Alert;

public class BaseController {
    private User user;

    public void setCurrentUser(User currentUser) {
        this.user = currentUser;
    }

    protected User getUser() {
        return user;
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
