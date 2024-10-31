package com.example.cm2601_cw_javafx;

import java.io.*;

public class UserStorage {
    private static final String FILE_NAME = "users.csv"; // Renamed for clarity

    public void registerUser(User user) {
        // Check if the file exists to write the header
        File file = new File(FILE_NAME);
        boolean fileExists = file.exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            // Write header if file did not exist before
            if (!fileExists) {
                writer.write("userID,username,password"); // Column names
                writer.newLine();
            }
            writer.write(user.getUserID() + "," + user.getUsername() + "," + user.getPassword());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            // Skip the header line
            reader.readLine(); // This will read and discard the header
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[1].equals(username) && userDetails[2].equals(password)) {
                    return true; // User is valid
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // User not found
    }
}
