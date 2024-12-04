package com.example.cm2601_cw_javafx.model;

import com.example.cm2601_cw_javafx.db.DBManager;

import java.sql.SQLException;
import java.util.*;

public class User extends SystemUser {
    private List<Category> selectedCategories;
    private List<ViewedArticle> viewedArticles;
    private List<Article> likedArticles;
    private List<Article> skippedArticles;


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





//    public void unlikeArticle(int articleID) {
//        dbManager.removeInteraction(userID, articleID);
//    }
//
//    public void skipArticle(int articleID) {
//        dbManager.addInteraction(getUserID(), articleID, "SKIP");
//
//    }
//
//    public void unskipArticle(int articleID) {
//        dbManager.removeInteraction(userID, articleID);
//    }
//
//
//    public boolean hasLikedArticle(int articleID) {
//        return dbManager.hasInteraction(userID, articleID, "LIKE");
//    }
//
//    public boolean hasSkippedArticle(int articleID) {
//        return dbManager.hasInteraction(userID, articleID, "SKIP");
//    }

    // ----------------------------

    public List<Article> getLikedArticles(int userID) throws SQLException {
        likedArticles = DBManager.getArticlesByInteractionQuery(userID, "LIKE");
        return likedArticles;
    }


    public List<Article> getSkippedArticles(int userID) throws SQLException {
        skippedArticles = DBManager.getArticlesByInteractionQuery(userID, "SKIP");
        return skippedArticles;
    }


    public List<ViewedArticle> getViewedArticles(int userId) {
        viewedArticles = DBManager.getViewedArticlesQuery(userId);
        return viewedArticles;
    }

//    public boolean hasLikedArticle(Article article) {
//        return likedArticles.contains(article);
//    }
//
//    public boolean hasSkippedArticle(Article article) {
//        return skippedArticles.contains(article);
//    }



//    public void removeLikedArticle(Article article) {
//        // Remove the article directly from the likedArticles list
//        likedArticles.remove(article);
//    }
//
//    public void removeSkippedArticle(Article article) {
//        // Remove the article directly from the skippedArticles list
//        skippedArticles.remove(article);
//    }
//
//    public void addLikedArticle(Article article) {
//        // Add the article to the list if it doesn't already exist
//        if (!likedArticles.contains(article)) {
//            likedArticles.add(article);
//        }
//    }
//
//    public void addSkippedArticle(Article article) {
//        // Add the article to the list if it doesn't already exist
//        if (!skippedArticles.contains(article)) {
//            skippedArticles.add(article);
//        }
//    }

//    public void likeArticle(Article article) {
//        if (!likedArticles.contains(article)) {
//            likedArticles.add(article);
//            skippedArticles.remove(article); // Ensure it’s not in skipped
//            dbManager.addInteraction(getUserID(), article.getArticleID(), "LIKE");
//        }
//    }
//
//    public void unlikeArticle(Article article) {
//        likedArticles.remove(article);
//        dbManager.removeInteraction(getUserID(), article.getArticleID());
//    }
//
//    public void skipArticle(Article article) {
//        if (!skippedArticles.contains(article)) {
//            skippedArticles.add(article);
//            likedArticles.remove(article); // Ensure it’s not in liked
//            dbManager.addInteraction(getUserID(), article.getArticleID(), "SKIP");
//        }
//    }
//
//    public void unskipArticle(Article article) {
//        skippedArticles.remove(article);
//        dbManager.removeInteraction(getUserID(), article.getArticleID());
//    }

    // ---------------------

//    public void likeArticle(Article article) {
//        if (!isArticleInList(likedArticles, article)) {
//            likedArticles.add(article);
//            removeArticleFromList(skippedArticles, article); // Ensure it’s not in skipped
//            dbManager.addInteraction(getUserID(), article.getArticleID(), "LIKE");
//        }
//    }
//
//    public void unlikeArticle(Article article) {
//        removeArticleFromList(likedArticles, article);
//        dbManager.removeInteraction(getUserID(), article.getArticleID());
//    }
//
//    public void skipArticle(Article article) {
//        if (!isArticleInList(skippedArticles, article)) {
//            skippedArticles.add(article);
//            removeArticleFromList(likedArticles, article); // Ensure it’s not in liked
//            dbManager.addInteraction(getUserID(), article.getArticleID(), "SKIP");
//        }
//    }
//
//    public void unskipArticle(Article article) {
//        removeArticleFromList(skippedArticles, article);
//        dbManager.removeInteraction(getUserID(), article.getArticleID());
//    }

//    private boolean isArticleInList(List<Article> articles, Article article) {
//        return articles.stream().anyMatch(a -> a.getArticleID() == article.getArticleID());
//    }
//
//    private void removeArticleFromList(List<Article> articles, Article article) {
//        articles.removeIf(a -> a.getArticleID() == article.getArticleID());
//    }
//
//    public boolean hasLikedArticle(Article article) {
//        for (Article a : likedArticles) {
//            if (a.getArticleID() == article.getArticleID()) {
//                return true; // Return true as soon as a match is found
//            }
//        }
//        return false; // Return false if no match is found
//    }
//
//    public boolean hasSkippedArticle(Article article) {
//        for (Article a : skippedArticles) {
//            if (a.getArticleID() == article.getArticleID()) {
//                return true; // Return true as soon as a match is found
//            }
//        }
//        return false; // Return false if no match is found
//    }

    // -------------

    public boolean hasLikedArticle(Article article) {
        // Check in-memory list first
        for (Article a : likedArticles) {
            if (a.getArticleID() == article.getArticleID()) {
                return true; // Found in the in-memory list
            }
        }

        // Check the database for past interactions if not found in the list
        return DBManager.findInteractionQuery(getUserID(), article.getArticleID(), "LIKE");

    }

    public boolean hasSkippedArticle(Article article) {
        // Check in-memory list first
        for (Article a : skippedArticles) {
            if (a.getArticleID() == article.getArticleID()) {
                return true; // Found in the in-memory list
            }
        }

        // Check the database for past interactions if not found in the list
        return DBManager.findInteractionQuery(getUserID(), article.getArticleID(), "SKIP");

    }

    //------------

    public void likeArticle(Article article) {
        boolean isInLikedList = false;
        for (Article a : likedArticles) {
            if (a.getArticleID() == article.getArticleID()) {
                isInLikedList = true;
                break;
            }
        }

        if (!isInLikedList) {
            likedArticles.add(article);
            DBManager.addInteractionQuery(getUserID(), article.getArticleID(), "LIKE");
        }
    }

    public void unlikeArticle(Article article) {
        for (int i = 0; i < likedArticles.size(); i++) {
            if (likedArticles.get(i).getArticleID() == article.getArticleID()) {
                likedArticles.remove(i);
                break;
            }
        }

        DBManager.removeInteractionQuery(getUserID(), article.getArticleID());
    }

    public void skipArticle(Article article) {
        boolean isInSkippedList = false;
        for (Article a : skippedArticles) {
            if (a.getArticleID() == article.getArticleID()) {
                isInSkippedList = true;
                break;
            }
        }

        if (!isInSkippedList) {
            skippedArticles.add(article);

            DBManager.addInteractionQuery(getUserID(), article.getArticleID(), "SKIP");
        }
    }

    public void unskipArticle(Article article) {
        for (int i = 0; i < skippedArticles.size(); i++) {
            if (skippedArticles.get(i).getArticleID() == article.getArticleID()) {
                skippedArticles.remove(i);
                break;
            }
        }

        DBManager.removeInteractionQuery(getUserID(), article.getArticleID());
    }

    public List<Category> getSelectedCategories(int userID) {
        try {
            selectedCategories = DBManager.getUserCategoriesQuery(userID);
        } catch (SQLException e) {
            System.out.println("Error retrieving selected categories for user: " + getUsername());

        }
        return selectedCategories;
    }

    public void updateSelectedCategories(int userID, List<Category> newCategories) {
        try {
            DBManager.updateUserCategoriesQuery(userID, newCategories);

            selectedCategories.clear();
            selectedCategories.addAll(newCategories);

            System.out.println("Selected categories updated successfully for user: " + getUsername());
        } catch (SQLException e) {
            System.out.println("Error updating selected categories for user: " + getUsername());

        }
    }

    public void updatePassword(int userID, String newPassword) {
        try {
            // Update the password in the database
            DBManager.updatePasswordQuery(userID, newPassword);
            // Update the password in memory
            setPassword(newPassword);

            System.out.println("Password updated successfully for user: " + getUsername());
        } catch (SQLException e) {
            System.out.println("Error updating password for user: " + getUsername());
            e.printStackTrace();
        }
    }


}


