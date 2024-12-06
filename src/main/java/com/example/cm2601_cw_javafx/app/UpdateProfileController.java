package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Category;
import com.example.cm2601_cw_javafx.model.User;

import com.example.cm2601_cw_javafx.service.SystemUserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpdateProfileController extends UserBaseController {
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

    // Manages user actions
    private final SystemUserManager systemUserManager = new SystemUserManager();

    private User currentUser;

    // Sets the current user
    @Override
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }


    // Initializes the user's details in update profile form
    public void initializeUserDetails(){
        usernameField.setText(currentUser.getUsername());

        List<Category> userCategories = currentUser.getSelectedCategories(currentUser.getUserID());

        setSelectedCategories(userCategories);


    }

    // Marks checkboxes for the user's selected categories
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

    // Handles the update of selected interests
    @FXML
    private void onUpdateInterestsButtonClick() {
        List<Category> selectedCategories = new ArrayList<>();
        if (categorySports.isSelected()) selectedCategories.add(Category.SPORTS);
        if (categoryBusiness.isSelected()) selectedCategories.add(Category.BUSINESS);
        if (categoryHealthLife.isSelected()) selectedCategories.add(Category.HEALTH_LIFESTYLE);
        if (categoryEntertainment.isSelected()) selectedCategories.add(Category.ENTERTAINMENT);
        if (categoryScienceTech.isSelected()) selectedCategories.add(Category.SCIENCE_TECH);
        if (categoryPolitics.isSelected()) selectedCategories.add(Category.POLITICS);
        if (categoryEducation.isSelected()) selectedCategories.add(Category.EDUCATION);

        // Ensure at least two categories are selected
        if (selectedCategories.size() < 2) {
            showError("Please select at least two categories.");
            return;
        }

        // Update the user's selected categories in the database
        currentUser.updateSelectedCategories(currentUser.getUserID(), selectedCategories);
        showAlert("Interests updated successfully!");
    }


    // Handles the update of user's password
    @FXML
    private void onUpdatePasswordButtonClick() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        try {
            if (!DBManager.equalsCurrentPasswordQuery(currentUser.getUsername(), currentPassword)) {
                showError("Current password is incorrect.");
                return;
            }

            if (systemUserManager.invalidPassword(newPassword)) {
                showError("Password must be at least 8 characters with letters and numbers.");
                return;
            }

            if (currentPassword.equals(newPassword)){
                showError("New password must not be the same as current password.");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                showError("New password and Confirm password does not match.");
                return;
            }

            currentUser.updatePassword(currentUser.getUserID(), newPassword);

            showAlert("Password updated successfully!");
            clearFields();

        } catch (SQLException e) {
            showError("Error updating password: " + e.getMessage());
        }
    }

    @Override
    public void clearFields(){
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    // Navigates back to the Home page
    public void goBackToHome() {
        try {
            // Load the home page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/home.fxml"));
            Parent root = loader.load();

            HomeController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Scene currentScene = rootPane.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");

        }
    }
}
