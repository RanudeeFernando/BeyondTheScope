BEYOND THE SCOPE - Personalized News Recommendation System by Ranudee Fernando (2330908)

This document provides instructions on how to configure and set up the MySQL database for this project on another machine. Follow these steps carefully to ensure successful database integration.

Prerequisites

Install MySQL Community Server:
Download and install MySQL Community Server 9.1.0 (Innovation MSI Installer) if you are using Windows.
During the installation process, also install MySQL Workbench.

Set up a Local Instance:
Create a local MySQL instance during installation.
Set a password for the root user. Remember this password, as it will be required later.


Database Configuration Steps

1. Create the Database Schema
Open MySQL Workbench and connect to your local MySQL instance using the root credentials.
Create a new schema (database) with the name:
news_recommendation_system_db

2. Import the Database Dump
Locate the dump files:
The dump files for the database are included outside the project folder in a directory called Dump20241206.zip. Ensure this folder is saved to your local computer and unzip this folder.

In MySQL Workbench:
Go to the Server menu and select Data Import.
Under Import Options, select Import from Dump Project Folder.
Provide the path to the Dump20241206 folder.
Click Start Import.
The database news_recommendation_system_db will be created with all required tables and data.

Project Integration
1. Configure Database Connection in Code
Open the project in IntelliJ IDEA.
Navigate to the DBManager class located in the db.module package.

Update the following fields to match your MySQL configuration:

URL: The URL for the database connection. 
jdbc:mysql://localhost:3306/news_recommendation_system_db
Ensure the port number is 3306. If you use a different port, update it accordingly.

User: Typically, this is root. Update it if you use a different username.

Password: Enter the password you set during MySQL installation.

Notes:
The URL usually remains the same unless a different port is configured.
Ensure the User and Password match your MySQL instance credentials.

Completion
After completing these steps, the database setup is complete, and the project should run successfully on your system.

