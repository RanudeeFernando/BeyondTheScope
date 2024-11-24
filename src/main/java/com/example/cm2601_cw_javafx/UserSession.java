package com.example.cm2601_cw_javafx;

public class UserSession {
    private static UserSession instance;
    private SystemUser loggedInUser;

//    private int userId;
//    private String username;

    private UserSession() {
    }

    // Method to get the single instance of UserSession
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

//    public void setUserID(int userId) {
//        this.userId = userId;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public int getUserID() {
//        return userId;
//    }
//
//    public String getUsername() {
//        return username;
//    }

    public void setLoggedInUser(SystemUser user) {
        this.loggedInUser = user;
    }

    public SystemUser getLoggedInUser() {
        return loggedInUser;
    }

    public void clearSession() {
//        userId = 0;
//        username = null;
        loggedInUser = null;
    }
}
