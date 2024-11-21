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

    // When using csv change instance variable to manageUser
    // private final ManageUser manageUser = new ManageUser();

    // When using database - SQLite
    // private final UserDatabaseManager userDatabaseManager = new UserDatabaseManager();

    // When using database - MySQL
    private final UserManager userManager = new UserManager();


    @FXML
    private ImageView imageViewLogo;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private CheckBox categoryWorld;
    @FXML
    private CheckBox categoryBusiness;
    @FXML
    private CheckBox categoryTechnology;
    @FXML
    private CheckBox categorySports;
    @FXML
    private CheckBox categoryScience;
    @FXML
    private CheckBox categoryAI;
    @FXML
    private Button signUpButton;



    public void initialize() {
        setLogoImage(imageViewLogo, "images/logo5.png");
    }

    @FXML
    public void registerUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        List<Category> selectedCategories = getSelectedCategories();

        String result = userManager.registerUser(username, password, confirmPassword, selectedCategories);
        showAlert(result);

        // Clear fields if registration is successful
        if (result.equals("User successfully registered!")) {
            clearFields();
            navigateToHomePage();
        }
    }

    private List<Category> getSelectedCategories() {
        List<Category> selectedCategories = new ArrayList<>();
        if (categoryWorld.isSelected()) selectedCategories.add(Category.WORLD);
        if (categoryBusiness.isSelected()) selectedCategories.add(Category.BUSINESS);
        if (categoryTechnology.isSelected()) selectedCategories.add(Category.TECHNOLOGY);
        if (categorySports.isSelected()) selectedCategories.add(Category.SPORTS);
        if (categoryScience.isSelected()) selectedCategories.add(Category.SCIENCE);
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
        categoryWorld.setSelected(false);
        categoryBusiness.setSelected(false);
        categoryTechnology.setSelected(false);
        categorySports.setSelected(false);
        categoryScience.setSelected(false);
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


