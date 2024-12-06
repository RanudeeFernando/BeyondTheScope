package com.example.cm2601_cw_javafx.model;

// Abstract class representing a system user
public abstract class SystemUser {
    protected int userID;
    protected String username;
    protected String password;

    // Constructor
    public SystemUser(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }


    // Getters and setters for userID
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    // Getters and setters for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getters and setters for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




}