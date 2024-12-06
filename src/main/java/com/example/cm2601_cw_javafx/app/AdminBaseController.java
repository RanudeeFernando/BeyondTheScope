package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.model.Admin;
import javafx.scene.control.Alert;

public class AdminBaseController {

    private Admin admin;

    // Sets the current admin
    public void setAdmin(Admin currentAdmin) {
        this.admin = currentAdmin;
    }

    // Retrieves the current admin
    protected Admin getAdmin() {
        return admin;
    }

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
}
