package com.example.cm2601_cw_javafx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/news_recommendation_system_db";

    // private static final String URL = "jdbc:mysql://localhost:3306/news_rec_db";
    private static final String USER = "root";
    private static final String PASSWORD = "ranu2004";


    public static Connection connectToDatabase() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return connection;

        } catch (SQLException e) {
            System.out.println("There was an error when connecting to MySQL database.");
            return null;
        }
    }


}
