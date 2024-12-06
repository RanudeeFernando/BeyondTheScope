package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.model.User;
import javafx.scene.control.Alert;

public class UserBaseController {
    private User user;

    // Sets the current user
    public void setCurrentUser(User currentUser) {
        this.user = currentUser;
    }

    // Retrieves the current user
    protected User getUser() {
        return user;
    }

    // Shows an informational alert
    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Shows an error alert
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    // Clears input fields (to be overridden by subclasses)
    public void clearFields(){}

}
