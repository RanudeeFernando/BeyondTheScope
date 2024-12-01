//package com.example.cm2601_cw_javafx;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class UserPreference {
//    private String userID;
//    private Set<Category> likedCategories;
//    private Set<Category> dislikedCategories;
//
//    public UserPreference(String userID) {
//        this.userID = userID;
//        this.likedCategories = new HashSet<>();
//        this.dislikedCategories = new HashSet<>();
//    }
//
//    public void addLikedCategory(Category category) {
//        likedCategories.add(category);
//        dislikedCategories.remove(category); // Ensures the category isn't in both sets
//    }
//
//    public void addDislikedCategory(Category category) {
//        dislikedCategories.add(category);
//        likedCategories.remove(category); // Ensures the category isn't in both sets
//    }
//
//    public Set<Category> getLikedCategories() {
//        return likedCategories;
//    }
//
//    public Set<Category> getDislikedCategories() {
//        return dislikedCategories;
//    }
//}
