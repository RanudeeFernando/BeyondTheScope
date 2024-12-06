package com.example.cm2601_cw_javafx.app;

import com.example.cm2601_cw_javafx.db.DBManager;
import com.example.cm2601_cw_javafx.model.Category;
import com.example.cm2601_cw_javafx.service.SystemUserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignUpControllerUser extends UserBaseController {
    private final DBManager DBManager = new DBManager();
    private final SystemUserManager systemUserManager = new SystemUserManager();

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private CheckBox categoryPolitics;
    @FXML
    private CheckBox categoryBusiness;
    @FXML
    private CheckBox categoryScienceTech;
    @FXML
    private CheckBox categorySports;
    @FXML
    private CheckBox categoryHealthLife;
    @FXML
    private CheckBox categoryEducation;
    @FXML
    private CheckBox categoryEntertainment;
    @FXML
    private Button signUpButton;


    @FXML
    public void onSignupButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        List<Category> selectedCategories = getSelectedCategories();

        String result = systemUserManager.registerUser(username, password, confirmPassword, selectedCategories);
        showAlert(result);


        if (result.equals("User successfully registered as: " + username)) {
            clearFields();
            navigateToLoginPage();
        }
    }

    private List<Category> getSelectedCategories() {
        List<Category> selectedCategories = new ArrayList<>();
        if (categoryPolitics.isSelected()) selectedCategories.add(Category.POLITICS);
        if (categoryBusiness.isSelected()) selectedCategories.add(Category.BUSINESS);
        if (categoryScienceTech.isSelected()) selectedCategories.add(Category.SCIENCE_TECH);
        if (categorySports.isSelected()) selectedCategories.add(Category.SPORTS);
        if (categoryHealthLife.isSelected()) selectedCategories.add(Category.HEALTH_LIFESTYLE);
        if (categoryEducation.isSelected()) selectedCategories.add(Category.EDUCATION);
        if (categoryEntertainment.isSelected()) selectedCategories.add(Category.ENTERTAINMENT);

        return selectedCategories;
    }


    @Override
    public void clearFields() {
        // Clear text fields
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();

        // Uncheck all category checkboxes
        categoryPolitics.setSelected(false);
        categoryBusiness.setSelected(false);
        categoryScienceTech.setSelected(false);
        categorySports.setSelected(false);
        categoryHealthLife.setSelected(false);
        categoryEducation.setSelected(false);
        categoryEntertainment.setSelected(false);
    }


    private void navigateToLoginPage() {

        try {
            // Load the home page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/login.fxml"));
            Parent root = loader.load();

            Scene scene = signUpButton.getScene();
            scene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to login page.");
            e.printStackTrace();
        }
    }

    @FXML
    private void goBackToMain(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cm2601_cw_javafx/fxml/main.fxml"));
            Parent root = loader.load();

            Scene scene = signUpButton.getScene();
            scene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Main page.");
            e.printStackTrace();
        }
    }

}


