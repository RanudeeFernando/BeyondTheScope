//package com.example.cm2601_cw_javafx.service;
//
//import com.example.cm2601_cw_javafx.model.SystemUser;
//
//public class SessionService {
//    private static SessionService instance;
//    private SystemUser loggedInUser;
//
//    private SessionService() {
//    }
//
//    // Method to get the single instance of SessionService
//    public static SessionService getInstance() {
//        if (instance == null) {
//            instance = new SessionService();
//        }
//        return instance;
//    }
//
//    public void setLoggedInUser(SystemUser user) {
//        this.loggedInUser = user;
//    }
//
//    public SystemUser getLoggedInUser() {
//        return loggedInUser;
//    }
//
//    public void clearSession() {
//        loggedInUser = null;
//    }
//}
