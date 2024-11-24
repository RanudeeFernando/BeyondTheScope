package com.example.cm2601_cw_javafx;

import java.util.*;

public class RegularUser extends SystemUser{
    private List<Category> selectedCategories;
    private List<Article> readingHistory;
    private List<Article> likedArticles;
    private List<Article> skippedArticles;

    public RegularUser(int userID, String username, String password) {
        super(userID, username, password);
    }

    public RegularUser(int userID, String username, String password, List<Category> selectedCategories) {
        super(userID, username, password);
        this.selectedCategories = selectedCategories;
    }

    @Override
    public String getRole() {
        return "User";
    }

    public String getSelectedCategoriesAsString() {
        return String.join(",", selectedCategories.stream().map(Category::name).toList());
    }



}
