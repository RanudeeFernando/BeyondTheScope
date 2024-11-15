package com.example.cm2601_cw_javafx;

public class UserSession {
    private static UserSession instance;

    private int userId; // Stores the user ID of the logged-in user
    private String username; // Stores the username of the logged-in user

    // Private constructor to prevent instantiation from outside
    private UserSession() {
    }

    // Method to get the single instance of UserSession
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Setters for user session information
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getters for user session information
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    // Method to clear session data (e.g., when logging out)
    public void clearSession() {
        userId = 0;
        username = null;
    }
}
