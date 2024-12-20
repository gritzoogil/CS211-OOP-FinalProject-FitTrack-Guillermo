package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/"; 
    private static final String USER = "root";  
    private static final String PASS = "852963123a"; 

    private DatabaseConnection() {}

    public static void setupDatabase() {
        Connection connection = null;
        Statement stmt = null;

        try {
            // 1. connect to MySQL Server
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = connection.createStatement();

            // 2. create table database if not exists
            String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS fittrack";
            stmt.executeUpdate(createDatabaseSQL);

            // 3. close connection and connect to database
            connection.close();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fittrack", USER, PASS);
            stmt = connection.createStatement();

            // 4. execute table scripts
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\GilBryan\\Documents\\Projects\\CS 2nd Year\\Final Project\\CS211-OOP-FinalProject-FitTrack-Guillermo\\FitTrack\\src\\schema.sql"));
            StringBuilder sqlBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sqlBuilder.append(line).append("\n");
            }
            String sqlScript = sqlBuilder.toString();

            // 5. split the scripts into lines
            String[] statements = sqlScript.split(";");
            for (String statement : statements) {
                if (!statement.trim().isEmpty()) {
                    stmt.executeUpdate(statement.trim());
                }
            }
            System.out.println("Database and tables created.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


     // establishes a connection to the MySQL database.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/fittrack?useSSL=false&serverTimezone=UTC", USER, PASS);
    }

    // closes the connection
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Failed to close the connection: " + e.getMessage());
            }
        }
    }
}
