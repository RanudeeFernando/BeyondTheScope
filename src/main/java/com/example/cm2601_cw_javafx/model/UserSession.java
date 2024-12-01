package com.example.cm2601_cw_javafx.model;

public class UserSession {
    private static UserSession instance;
    private SystemUser loggedInUser;

    private UserSession() {
    }

    // Method to get the single instance of UserSession
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setLoggedInUser(SystemUser user) {
        this.loggedInUser = user;
    }

    public SystemUser getLoggedInUser() {
        return loggedInUser;
    }

    public void clearSession() {
        loggedInUser = null;
    }
}
