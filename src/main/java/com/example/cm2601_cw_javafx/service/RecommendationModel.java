package com.example.cm2601_cw_javafx.service;

import com.example.cm2601_cw_javafx.db.UserDBManager;
import net.librec.conf.Configuration;
import net.librec.data.DataModel;
import net.librec.data.model.TextDataModel;
import net.librec.recommender.Recommender;
import net.librec.recommender.RecommenderContext;
import net.librec.recommender.cf.UserKNNRecommender;
import net.librec.recommender.item.RecommendedItem;
import net.librec.similarity.RecommenderSimilarity;
import net.librec.similarity.PCCSimilarity;


import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecommendationModel {
    private static final String CSV_FILE_PATH = "user_interactions.csv";

    public static void generateCSV() {
//        String query = """
//        SELECT
//            v.userID,
//            v.articleID,
//            (COUNT(DISTINCT v.viewedAt) * 1.0 +
//             COALESCE(i.like_count, 0) * 5.0 +
//             COALESCE(i.skip_count, 0) * -2.0) AS full_weight
//        FROM user_viewed_article v
//        LEFT JOIN (
//            SELECT
//                userID,
//                articleID,
//                SUM(CASE WHEN interactionType = 'SKIP' THEN 1 ELSE 0 END) AS skip_count,
//                SUM(CASE WHEN interactionType = 'LIKE' THEN 1 ELSE 0 END) AS like_count
//            FROM user_article_interaction
//            GROUP BY userID, articleID
//        ) i ON v.userID = i.userID AND v.articleID = i.articleID
//        GROUP BY v.userID, v.articleID;
//    """;

        String query = """
    SELECT
        v.userID,
        v.articleID,
        (COUNT(DISTINCT v.viewedAt) * 1.0 +
         COALESCE(i.like_count, 0) * 3.0) AS full_weight
    FROM user_viewed_article v
    LEFT JOIN (
        SELECT
            userID,
            articleID,
            SUM(CASE WHEN interactionType = 'LIKE' THEN 1 ELSE 0 END) AS like_count
        FROM user_article_interaction
        GROUP BY userID, articleID
    ) i ON v.userID = i.userID AND v.articleID = i.articleID
    GROUP BY v.userID, v.articleID;
    """;

        try (Connection conn = UserDBManager.connectToDatabase();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query);
             FileWriter csvWriter = new FileWriter(CSV_FILE_PATH)) {

            // Write data without a header row
            while (rs.next()) {
                int userID = rs.getInt("userID");
                int articleID = rs.getInt("articleID");
                double fullWeight = rs.getDouble("full_weight");

                csvWriter.append(String.format("%d,%d,%.2f\n", userID, articleID, fullWeight));
            }

            System.out.println("CSV file generated successfully: " + CSV_FILE_PATH);

        } catch (SQLException | IOException e) {
            System.err.println("Error generating CSV: " + e.getMessage());
        }
    }

    public static List<RecommendedItem> generateRecommendations() {
        List<RecommendedItem> recommendations = new ArrayList<>();

        try {
            // Generate CSV file
            generateCSV();

            // Configuration
            Configuration conf = new Configuration();
            conf.set("dfs.data.dir", ".");
            conf.set("data.model.splitter", "ratio");
            conf.set("data.splitter.trainset.ratio", "0.8");
            conf.set("data.column.format", "UIR");
            conf.set("data.model.format", "text");
            conf.set("data.input.path", CSV_FILE_PATH);

            // Add required configuration for UserKNNRecommender
            conf.set("rec.neighbors.knn.number", "10");
            // conf.set("rec.similarity.class", "cosine"); // Similarity metric

            // Data Model
            DataModel dataModel = new TextDataModel(conf);
            dataModel.buildDataModel();


            RecommenderSimilarity similarity = new PCCSimilarity();
            similarity.buildSimilarityMatrix(dataModel);

            RecommenderContext context = new RecommenderContext(conf, dataModel);
            context.setSimilarity(similarity); // Set the similarity object

            // Initialize the recommender
            Recommender recommender = new UserKNNRecommender();
            recommender.setContext(context);

            // Train and generate recommendations
            recommender.recommend(context);

            // Retrieve recommendations
            recommendations = recommender.getRecommendedList();

            for (RecommendedItem recommendation : recommendations) {
                System.out.println("User: " + recommendation.getUserId() +
                        ", Item: " + recommendation.getItemId() +
                        ", Score: " + recommendation.getValue());
            }




        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return recommendations;
    }

    public static void main(String[] args) {
        try {
            // Generate CSV file
            generateCSV();

            // Configuration
            Configuration conf = new Configuration();
            conf.set("dfs.data.dir", ".");
            conf.set("data.model.splitter", "ratio");
            conf.set("data.splitter.trainset.ratio", "0.8");
            conf.set("data.column.format", "UIR");
            conf.set("data.model.format", "text");
            conf.set("data.input.path", CSV_FILE_PATH);

            // Add required configuration for UserKNNRecommender
            conf.set("rec.neighbors.knn.number", "10");
            conf.set("rec.similarity.class", "cosine"); // Similarity metric

            // Data Model
            DataModel dataModel = new TextDataModel(conf);
            dataModel.buildDataModel();

            // Initialize similarity metric
            RecommenderSimilarity similarity = new PCCSimilarity();
            similarity.buildSimilarityMatrix(dataModel);

            // Create the RecommenderContext
            RecommenderContext context = new RecommenderContext(conf, dataModel);
            context.setSimilarity(similarity); // Set the similarity object

            // Initialize the recommender
            Recommender recommender = new UserKNNRecommender();
            recommender.setContext(context);

            // Train and generate recommendations
            recommender.recommend(context);

            // Retrieve recommendations
            List<RecommendedItem> recommendations = recommender.getRecommendedList();
            for (RecommendedItem recommendation : recommendations) {
                System.out.println("User: " + recommendation.getUserId() +
                        ", Item: " + recommendation.getItemId() +
                        ", Score: " + recommendation.getValue());
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }


    }
}
