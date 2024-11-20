//package com.example.cm2601_cw_javafx;
//
//import java.io.*;
//import java.util.List;
//import java.util.UUID;
//
//public class ManageUser {
//    private static final String FILE_NAME = "users.csv"; // Renamed for clarity
//
//
//    public String registerUser(String username, String password, String confirmPassword, List<Category> selectedCategories) {
//        if (!validateUsername(username)){
//            return "Username must be at least 8 characters and only contain letters.";
//        }
//        if (usernameExists(username)) {
//            return "Username already exists!";
//        }
//        if (!validatePassword(password)) {
//            return "Password must be at least 8 characters with letters and numbers.";
//        }
//        if (!password.equals(confirmPassword)) {
//            return "Passwords do not match!";
//        }
//        if (selectedCategories.size() < 2) {
//            return "Please select at least two categories.";
//        }
//
//        String userID = UUID.randomUUID().toString();
//        User user = new User(userID, username, password, selectedCategories);
//        storeUser(user);
//
//        return "User successfully registered!";
//    }
//
//    private boolean validateUsername(String username) {
//        return username.length() >= 8 && username.matches("[A-Za-z]+");
//    }
//
//    private boolean validatePassword(String password) {
//        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
//    }
//
//    public boolean usernameExists(String username) {
//        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] userDetails = line.split(",");
//                if (userDetails[1].equals(username)) {
//                    return true;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    private void storeUser(User user) {
//        File file = new File(FILE_NAME);
//        boolean fileExists = file.exists();
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
//            if (!fileExists) {
//                writer.write("userID,username,password,categories");
//                writer.newLine();
//            }
//            writer.write(user.getUserID() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getSelectedCategoriesAsString());
//            writer.newLine();
//
//        } catch (IOException e) {
//            System.out.println("The file \"users.csv\" does not exist.");
//        }
//    }
//
//}
//
//
