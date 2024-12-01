package com.example.cm2601_cw_javafx.model;

import java.util.*;

public class User extends SystemUser {
    private List<Category> selectedCategories;
    private List<UserViewedArticle> viewedArticles;
    private List<Article> likedArticles;
    private List<Article> skippedArticles;
    private List<Article> recommendedArticles;

    public User(int userID, String username, String password) {
        super(userID, username, password);
        this.selectedCategories = new ArrayList<>();
        this.viewedArticles = new ArrayList<>();
        this.likedArticles = new ArrayList<>();
        this.skippedArticles = new ArrayList<>();
    }


    public User(int userID, String username, String password, List<Category> selectedCategories) {
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

    public void likeArticle(Article article) {
        if (!likedArticles.contains(article)) {
            likedArticles.add(article);
            skippedArticles.remove(article);
        }
    }

    public void unlikeArticle(Article article) {
        likedArticles.remove(article);
    }

    public void skipArticle(Article article) {
        if (!skippedArticles.contains(article)) {
            skippedArticles.add(article);
            likedArticles.remove(article);
        }
    }

    public void unskipArticle(Article article) {
        skippedArticles.remove(article);
    }

    public boolean hasLikedArticle(Article article) {
        return likedArticles.contains(article);
    }

    public boolean hasSkippedArticle(Article article) {
        return skippedArticles.contains(article);
    }

    public List<Article> getLikedArticles() {
        return new ArrayList<>(likedArticles);
    }

    public List<Article> getSkippedArticles() {
        return new ArrayList<>(skippedArticles);
    }

    public void addRecommendedArticle(Article article) {
        if (!recommendedArticles.contains(article)) {
            recommendedArticles.add(article);
        }
    }

    public void removeRecommendedArticle(Article article) {
        recommendedArticles.remove(article);
    }

    public List<Article> getRecommendedArticles() {
        return new ArrayList<>(recommendedArticles);
    }

    public boolean isRecommended(Article article) {
        return recommendedArticles.contains(article);
    }


    public void addToViewedArticles(UserViewedArticle viewedArticle) {
        viewedArticles.add(viewedArticle);
    }

    public List<UserViewedArticle> getViewedArticles() {
        return new ArrayList<>(viewedArticles);
    }
}


