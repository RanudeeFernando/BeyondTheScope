package com.example.cm2601_cw_javafx;

import java.sql.SQLException;
import java.util.List;

public class SystemUserManager {

    private final UserDAO userDAO;

    public SystemUserManager(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String registerUser(String username, String password, String confirmPassword, List<Category> selectedCategories) {
        if (!validateUsername(username)) {
            return "Username must be at least 4 characters and only contain letters.";
        }

        try {
            if (userDAO.usernameExists(username)) {
                return "Username already exists!";
            }
        } catch (SQLException e) {
            return "Database error during username check: " + e.getMessage();
        }

        if (!validatePassword(password)) {
            return "Password must be at least 8 characters with letters and numbers.";
        }

        if (!password.equals(confirmPassword)) {
            return "Passwords do not match!";
        }

        if (selectedCategories.size() < 2) {
            return "Please select at least two categories.";
        }

        try {
            int userId = userDAO.insertUser(username, password);
            userDAO.insertUserPreferences(userId, selectedCategories);
            return "User successfully registered!";
        } catch (SQLException e) {
            return "Registration failed: " + e.getMessage();
        }
    }

    public String authenticateUser(String username, String password) {
        try {
            Object[] userInfo = userDAO.getUserInfo(username);
            if (userInfo == null) {
                return "Username not found.";
            }

            String storedPassword = (String) userInfo[1];

            if (!password.equals(storedPassword)) {
                return "Invalid password.";
            }

            return "Login successful!";
        } catch (SQLException e) {
            return "Login failed due to database error: " + e.getMessage();
        }
    }

    public SystemUser getUserByRole(String username) {
        try {
            Object[] userInfo = userDAO.getUserInfo(username);
            if (userInfo == null) {
                return null;
            }

            int userID = (int) userInfo[0];
            String password = (String) userInfo[1];
            String role = (String) userInfo[2];

            if ("ADMIN".equalsIgnoreCase(role)) {
                return new Admin(userID, username, password);
            }
            else if ("USER".equalsIgnoreCase(role)) {
                return new RegularUser(userID, username, password);
            }
            else {
                System.out.println("Unknown role: " + role);
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user by role: " + e.getMessage());
            return null;
        }
    }

    public int getUserIdByUsername(String username) {
        try {
            return userDAO.getUserIdByUsername(username);
        } catch (SQLException e) {
            System.out.println("Error retrieving user ID by username: " + e.getMessage());
            return -1;
        }
    }

//    public SystemUser loginUser(String username, String password) {
//        try {
//
//            Object[] userInfo = userDAO.getUserInfo(username);
//            if (userInfo == null) {
//                System.out.println("Username not found.");
//                return null;
//            }
//
//            int userID = (int) userInfo[0];
//            String storedPassword = (String) userInfo[1];
//            String role = (String) userInfo[2];
//
//            if (!password.equals(storedPassword)) {
//                System.out.println("Invalid password.");
//                return null;
//            }
//
//            if ("ADMIN".equalsIgnoreCase(role)) {
//                return new Admin(userID, username, password);
//            } else if ("USER".equalsIgnoreCase(role)) {
//                return new RegularUser(userID, username, password);
//            } else {
//                System.out.println("Unknown role: " + role);
//                return null;
//            }
//        } catch (SQLException e) {
//            System.out.println("Login failed due to database error: " + e.getMessage());
//            return null;
//        }
//    }

    public SystemUser loginUser(String username, String password) {
        try {
            Object[] userInfo = userDAO.getUserInfo(username);
            if (userInfo == null) {
                System.out.println("Username not found.");
                return null;
            }

            int userID = (int) userInfo[0];
            String storedPassword = (String) userInfo[1];
            String role = (String) userInfo[2];

            if (!password.equals(storedPassword)) {
                System.out.println("Invalid password.");
                return null;
            }

            // Return the correct SystemUser type
            if ("ADMIN".equalsIgnoreCase(role)) {
                return new Admin(userID, username, password);
            } else if ("USER".equalsIgnoreCase(role)) {
                return new RegularUser(userID, username, password);
            } else {
                System.out.println("Unknown role: " + role);
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Login failed due to database error: " + e.getMessage());
            return null;
        }
    }


    private boolean validateUsername(String username) {
        return username.length() >= 4 && username.matches("[A-Za-z]+");
    }

    private boolean validatePassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
    }

    public void likeArticle(int userId, int articleId) {
        try {
            userDAO.likeArticle(userId, articleId);
            System.out.println("Article liked successfully!");
        } catch (SQLException e) {
            System.out.println("Error liking article: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void unlikeArticle(int userId, int articleId) {
        try {
            userDAO.unlikeArticle(userId, articleId);
            System.out.println("Article unliked successfully!");
        } catch (SQLException e) {
            System.out.println("Error unliking article: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean hasLikedArticle(int userId, int articleId) {
        try {
            return userDAO.hasLikedArticle(userId, articleId);
        } catch (SQLException e) {
            System.out.println("Error checking if article is liked: " + e.getMessage());
            return false;
        }
    }

    public List<Article> getLikedArticles(int userId) {
        try {
            return userDAO.getLikedArticles(userId);
        } catch (SQLException e) {
            System.out.println("Error retrieving liked articles: " + e.getMessage());
            return List.of();
        }
    }

    public void skipArticle(int userId, int articleId) {
        try {
            userDAO.skipArticle(userId, articleId);
            System.out.println("Article skipped successfully!");
        } catch (SQLException e) {
            System.out.println("Error skipping article: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void unskipArticle(int userId, int articleId) {
        try {
            userDAO.unskipArticle(userId, articleId);
            System.out.println("Article unskipped successfully!");
        } catch (SQLException e) {
            System.out.println("Error unskipping article: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean hasSkippedArticle(int userId, int articleId) {
        try {
            return userDAO.hasSkippedArticle(userId, articleId);
        } catch (SQLException e) {
            System.err.println("Error checking if article is skipped: " + e.getMessage());
            // Optionally, log the exception or take appropriate action
            return false; // Return a default value in case of an error
        }
    }

    public List<Article> getSkippedArticles(int userId) {
        try {
            return userDAO.getSkippedArticles(userId);
        } catch (SQLException e) {
            System.out.println("Error retrieving skipped articles: " + e.getMessage());
            return List.of();
        }
    }





}