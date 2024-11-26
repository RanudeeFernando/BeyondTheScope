package com.example.cm2601_cw_javafx;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class RecommendationEngine {

    private static final String CSV_FILE_PATH = "user_interactions.csv";

    public static void generateCSV() {
        String query = """
            SELECT
                v.userID,
                v.articleID,
                (COUNT(DISTINCT v.viewedAt) * 1.0 +
                 COALESCE(i.like_count, 0) * 5.0 +
                 COALESCE(i.skip_count, 0) * -2.0) AS full_weight
            FROM user_viewed_article v
            LEFT JOIN (
                SELECT
                    userID,
                    articleID,
                    SUM(CASE WHEN interactionType = 'SKIP' THEN 1 ELSE 0 END) AS skip_count,
                    SUM(CASE WHEN interactionType = 'LIKE' THEN 1 ELSE 0 END) AS like_count
                FROM user_article_interaction
                GROUP BY userID, articleID
            ) i ON v.userID = i.userID AND v.articleID = i.articleID
            GROUP BY v.userID, v.articleID;
        """;

        // Use the MySQLConnection class to get a database connection
        try (Connection conn = MySQLConnection.connectToDatabase();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query);
             FileWriter csvWriter = new FileWriter(CSV_FILE_PATH)) {

            // Write the header to the CSV
            csvWriter.append("userID,articleID,full_weight\n");

            // Process the result set and write to the CSV file
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


    public static void main(String[] args) throws Exception {

        System.out.println("Generating CSV...");
        generateCSV();


        File csvFile = new File("user_interactions.csv");
        DataModel dataModel = new FileDataModel(csvFile);
        System.out.println("Data model loaded successfully.");

        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);

        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, dataModel);

        Recommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);

        int userId = 1; // Change this to test recommendations for other users
        int numRecommendations = 5; // Number of recommendations to generate
        List<RecommendedItem> recommendations = recommender.recommend(userId, numRecommendations);

        System.out.println("Recommended Articles for User " + userId + ":");
        for (RecommendedItem recommendation : recommendations) {
            System.out.printf("Article ID: %d, Score: %.2f%n", recommendation.getItemID(), recommendation.getValue());
        }
    }
}
