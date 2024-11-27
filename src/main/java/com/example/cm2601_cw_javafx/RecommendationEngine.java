package com.example.cm2601_cw_javafx;

import org.lenskit.api.Recommender;
import org.lenskit.api.Result;
import org.lenskit.api.ResultList;
import org.lenskit.api.ItemRecommender;
import org.lenskit.config.LenskitConfiguration;
import org.lenskit.config.LenskitRecommender;
import org.lenskit.data.dao.DataAccessObject;
import org.lenskit.config.text.CSVFileRatingDAO;
import org.lenskit.knn.user.UserUserItemScorer;

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

        try (Connection conn = MySQLConnection.connectToDatabase();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query);
             FileWriter csvWriter = new FileWriter(CSV_FILE_PATH)) {

            csvWriter.append("userID,articleID,full_weight\n");

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
        // Load the user-item interaction data from a CSV file
        File dataFile = new File("user_interactions.csv");
        DataAccessObject dao = CSVFileRatingDAO.open(dataFile, ',');

        // Configure LensKit
        LenskitConfiguration config = new LenskitConfiguration();
        config.bind(ItemRecommender.class).to(UserUserItemScorer.class);  // Use user-user collaborative filtering
        config.addRoot(DataAccessObject.class).to(dao);

        // Build the recommender
        try (Recommender recommender = LenskitRecommender.build(config)) {
            ItemRecommender itemRecommender = recommender.getItemRecommender();
            assert itemRecommender != null;

            // Generate recommendations for a specific user
            long userId = 1;  // Replace with the user ID you want recommendations for
            int numRecommendations = 5;

            ResultList recommendations = itemRecommender.recommendWithDetails(userId, numRecommendations);
            System.out.println("Recommendations for User " + userId + ":");
            for (Result result : recommendations) {
                System.out.printf("Item ID: %d, Score: %.2f%n", result.getId(), result.getScore());
            }
        }
    }
}
