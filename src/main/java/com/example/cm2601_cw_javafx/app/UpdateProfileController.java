package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpdateProfileController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField currentPasswordField;
    @FXML
    private TextField newPasswordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private CheckBox categorySports;
    @FXML
    private CheckBox categoryBusiness;
    @FXML
    private CheckBox categoryHealthLife;
    @FXML
    private CheckBox categoryEntertainment;
    @FXML
    private CheckBox categoryScienceTech;
    @FXML
    private CheckBox categoryPolitics;
    @FXML
    private CheckBox categoryEducation;

    private final UserDAO userDAO = new UserDAO();
    private final SystemUserManager systemUserManager = new SystemUserManager(userDAO);
    User currentUser = (User) UserSession.getInstance().getLoggedInUser();

    public void initialize(){
        usernameField.setText(currentUser.getUsername());
        try {
            List<Category> userCategories = userDAO.getUserPreferences(currentUser.getUserID());
            setSelectedCategories(userCategories);
            System.out.println(userCategories);
        } catch (SQLException e) {
            showError("Error loading user preferences: " + e.getMessage());
        }

    }

    private void setSelectedCategories(List<Category> userCategories) {
        for (Category category : userCategories) {
            if (category == Category.SPORTS) {
                categorySports.setSelected(true);
            } else if (category == Category.BUSINESS) {
                categoryBusiness.setSelected(true);
            } else if (category == Category.HEALTH_LIFESTYLE) {
                categoryHealthLife.setSelected(true);
            } else if (category == Category.ENTERTAINMENT) {
                categoryEntertainment.setSelected(true);
            } else if (category == Category.SCIENCE_TECH) {
                categoryScienceTech.setSelected(true);
            } else if (category == Category.POLITICS) {
                categoryPolitics.setSelected(true);
            } else if (category == Category.EDUCATION){
                categoryEducation.setSelected(true);
            }
        }
    }

    @FXML
    private void updateInterests() throws SQLException {
        List<Category> selectedCategories = new ArrayList<>();
        if (categorySports.isSelected()) selectedCategories.add(Category.SPORTS);
        if (categoryBusiness.isSelected()) selectedCategories.add(Category.BUSINESS);
        if (categoryHealthLife.isSelected()) selectedCategories.add(Category.HEALTH_LIFESTYLE);
        if (categoryEntertainment.isSelected()) selectedCategories.add(Category.ENTERTAINMENT);
        if (categoryScienceTech.isSelected()) selectedCategories.add(Category.SCIENCE_TECH);
        if (categoryPolitics.isSelected()) selectedCategories.add(Category.POLITICS);
        if (categoryEducation.isSelected()) selectedCategories.add(Category.EDUCATION);

        if (selectedCategories.size() < 2) {
            showError("Please select at least two categories.");
            return;
        }

        userDAO.updateUserPreferences(currentUser.getUserID(), selectedCategories);
        showSuccess("Interests updated successfully!");
    }



    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void updatePassword() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        try {
            if (!userDAO.validateCurrentPassword(currentUser.getUsername(), currentPassword)) {
                showError("Current password is incorrect.");
                return;
            }

            if (!systemUserManager.validatePassword(newPassword)) {
                showError("Password must be at least 8 characters with letters and numbers.");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                showError("Passwords do not match.");
                return;
            }

            userDAO.updatePassword(currentUser.getUserID(), newPassword);
            showSuccess("Password updated successfully!");
            currentPasswordField.clear();
            newPasswordField.clear();
            currentPasswordField.clear();

        } catch (SQLException e) {
            showError("Error updating password: " + e.getMessage());
        }
    }

    public void goBackToHome() {
        try {
            // Load the home page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
            e.printStackTrace();
        }
    }
}
