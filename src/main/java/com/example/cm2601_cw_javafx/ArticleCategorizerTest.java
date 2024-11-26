//package com.example.cm2601_cw_javafx;
//
//import java.util.List;
//
//public class ArticleCategorizerTest {
//
//    public static void main(String[] args) {
//        try {
//            // Fetch articles
//            ArticleService service = new ArticleService();
//            List<Article> articles = service.getAllArticles();
//
//            // Filter articles with an unknown category
//            List<Article> unknownCategoryArticles = articles.stream()
//                    .filter(article -> article.getCategory() == Category.UNKNOWN)
//                    .toList();
//
//            if (unknownCategoryArticles.isEmpty()) {
//                System.out.println("No articles left to categorize.");
//                return;
//            }
//
//            // Categorize only unknown-category articles
//            ArticleCategorizer categorizer = new ArticleCategorizer();
//            // categorizer.categorizeArticles(unknownCategoryArticles);
//
//            categorizer.categorizeUnknownArticles();
//
//            // Display results for categorized unknown articles
//            unknownCategoryArticles.forEach(article -> {
//                System.out.println("Article ID: " + article.getArticleID());
//                System.out.println("Article Content: " + article.getContent());
//                System.out.println("Predicted Category: " + article.getCategory());
//                System.out.println("-----");
//            });
//        } catch (Exception e) {
//            // Handle unexpected errors
//            System.err.println("An error occurred: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//
//    }
//}
