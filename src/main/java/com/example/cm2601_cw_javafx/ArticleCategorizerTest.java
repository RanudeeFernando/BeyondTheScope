package com.example.cm2601_cw_javafx;

import java.util.List;

public class ArticleCategorizerTest {

    public static void main(String[] args) {

        ArticleService service = new ArticleService();
        List<Article> articles = service.getAllArticles();

        ArticleCategorizer categorizer = new ArticleCategorizer();

        categorizer.categorizeArticles(articles);

        for (Article article : articles) {
            System.out.println("Article ID: " + article.getArticleID());
            System.out.println("Article Content: " + article.getContent());
            System.out.println("Predicted Category: " + article.getCategory());
            System.out.println("-----");
        }
    }
}
