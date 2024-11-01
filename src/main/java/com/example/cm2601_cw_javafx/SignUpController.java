package com.example.cm2601_cw_javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignUpController extends BaseController{

    private final ManageUser manageUser = new ManageUser();
    private final UserDatabaseManager userDatabaseManager = new UserDatabaseManager();

    @FXML
    private ImageView imageViewLogo;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private CheckBox categoryHealth;
    @FXML
    private CheckBox categoryBusiness;
    @FXML
    private CheckBox categoryTechnology;
    @FXML
    private CheckBox categoryEntertainment;
    @FXML
    private CheckBox categorySports;
    @FXML
    private CheckBox categoryNature;
    @FXML
    private CheckBox categoryPolitics;
    @FXML
    private CheckBox categoryScience;
    @FXML
    private CheckBox categoryEducation;
    @FXML
    private CheckBox categoryAI;
    @FXML
    private Button signUpButton;



    public void initialize() {
        setLogoImage(imageViewLogo, "logo5.png");
    }

    @FXML
    public void registerUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        List<Category> selectedCategories = getSelectedCategories();

        String result = userDatabaseManager.registerUser(username, password, confirmPassword, selectedCategories);
        showAlert(result);

        // Clear fields if registration is successful
        if (result.equals("User successfully registered!")) {
            clearFields();
            navigateToHomePage();
        }
    }

    private List<Category> getSelectedCategories() {
        List<Category> selectedCategories = new ArrayList<>();
        if (categoryHealth.isSelected()) selectedCategories.add(Category.HEALTH);
        if (categoryBusiness.isSelected()) selectedCategories.add(Category.BUSINESS);
        if (categoryTechnology.isSelected()) selectedCategories.add(Category.TECHNOLOGY);
        if (categoryEntertainment.isSelected()) selectedCategories.add(Category.ENTERTAINMENT);
        if (categorySports.isSelected()) selectedCategories.add(Category.SPORTS);
        if (categoryNature.isSelected()) selectedCategories.add(Category.NATURE);
        if (categoryPolitics.isSelected()) selectedCategories.add(Category.POLITICS);
        if (categoryScience.isSelected()) selectedCategories.add(Category.SCIENCE);
        if (categoryEducation.isSelected()) selectedCategories.add(Category.EDUCATION);
        if (categoryAI.isSelected()) selectedCategories.add(Category.AI);

        return selectedCategories;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        // Clear text fields
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();

        // Uncheck all category checkboxes
        categoryHealth.setSelected(false);
        categoryBusiness.setSelected(false);
        categoryTechnology.setSelected(false);
        categoryEntertainment.setSelected(false);
        categorySports.setSelected(false);
        categoryNature.setSelected(false);
        categoryPolitics.setSelected(false);
        categoryScience.setSelected(false);
        categoryEducation.setSelected(false);
        categoryAI.setSelected(false);
    }

    private void navigateToHomePage() {
        try {
            // Load the home page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            // Update the existing scene's root
            Scene scene = signUpButton.getScene();
            scene.setRoot(root);

        } catch (IOException e) {
            System.out.println("An error occurred while redirecting to Home page.");
        }
    }

}


