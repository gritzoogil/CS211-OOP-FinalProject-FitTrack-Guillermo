package dao;

import model.CalorieEntry;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalorieEntryDAO {

    public static void addCalorieEntry(CalorieEntry entry) throws SQLException {
        String query = "INSERT INTO CalorieEntry (user_id, date, calories, proteins, carbs, fats) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
        	
            for (int i = 0; i < 7; i++) {
                LocalDate currentDate = entry.getDate().plusDays(i);
                stmt.setInt(1, entry.getUserId());
                stmt.setDate(2, java.sql.Date.valueOf(currentDate));
                stmt.setInt(3, entry.getCalories());
                stmt.setInt(4, entry.getProteins());
                stmt.setInt(5, entry.getCarbs());
                stmt.setInt(6, entry.getFats());
                stmt.addBatch(); 
            }
            stmt.executeBatch();
        }
    }

    public List<CalorieEntry> getCalorieEntriesByUserId(int userId) throws SQLException {
        List<CalorieEntry> entries = new ArrayList<>();
        String query = "SELECT * FROM CalorieEntry WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                entries.add(new CalorieEntry(
                        rs.getInt("user_id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("calories"),
                        rs.getInt("proteins"),
                        rs.getInt("carbs"),
                        rs.getInt("fats")
                ));
            }
        }
        return entries;
    }
    
    public CalorieEntry getCalorieEntryForTodayByUserId(int userId) throws SQLException {
        LocalDate today = LocalDate.now();
        String query = "SELECT * FROM CalorieEntry WHERE user_id = ? AND date = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, java.sql.Date.valueOf(today));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new CalorieEntry(
                        rs.getInt("user_id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("calories"),
                        rs.getInt("proteins"),
                        rs.getInt("carbs"),
                        rs.getInt("fats")
                );
            }
        }
        return null;
    }
 
    public static int getCaloriesByUserId(int userId) throws SQLException {
        LocalDate today = LocalDate.now();
        String query = "SELECT calories FROM CalorieEntry WHERE user_id = ? AND date = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, java.sql.Date.valueOf(today));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("calories"); 
            }
        }
        return -1; 
    }
    
    public static int getCaloriesByUserIdOther(int userId) throws SQLException {
        LocalDate today = LocalDate.now();
        String query = "SELECT calories FROM CalorieEntry WHERE user_id = ? AND date = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, java.sql.Date.valueOf(today.minusDays(1)));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("calories"); 
            }
        }
        return -1; 
    }
    
    public static void updateCalorieEntryValues(int userId, int calories, int proteins, int carbs, int fats) throws SQLException {
        LocalDate today = LocalDate.now();
        String query = "UPDATE CalorieEntry SET calories = ?, proteins = ?, carbs = ?, fats = ? WHERE user_id = ? AND date = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, calories);
            stmt.setInt(2, proteins);
            stmt.setInt(3, carbs);
            stmt.setInt(4, fats);
            stmt.setInt(5, userId);
            stmt.setDate(6, java.sql.Date.valueOf(today));
            stmt.executeUpdate();
        }
    }
    
    public LocalDate getLatestDateByUserId(int userId) throws SQLException {
        String query = "SELECT MAX(date) AS latest_date FROM CalorieEntry WHERE user_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDate("latest_date").toLocalDate();
            }
        }
        return null;
    }
}
