package com.example.cm2601_cw_javafx.model;

public abstract class SystemUser {
    protected int userID;
    protected String username;
    protected String password;

    public SystemUser(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public SystemUser(int userID, String username){
        this.userID = userID;
        this.username = username;
        this.password = null;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public abstract String getRole();


}