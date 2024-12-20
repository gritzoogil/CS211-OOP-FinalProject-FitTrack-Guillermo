package dao;

import model.WeightEntry;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WeightEntryDAO {

    public static void addWeightEntry(WeightEntry entry) throws SQLException {
        String query = "INSERT INTO WeightEntry (user_id, date, weight) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, entry.getUserId());
            stmt.setDate(2, new java.sql.Date(entry.getDate().getTime()));
            stmt.setDouble(3, entry.getWeight());
            stmt.executeUpdate();
        }
    }

    public List<WeightEntry> getWeightEntriesByUserId(int userId) throws SQLException {
        List<WeightEntry> entries = new ArrayList<>();
        String query = "SELECT * FROM WeightEntry WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                entries.add(new WeightEntry(
                    rs.getInt("user_id"),
                    rs.getDate("date"),
                    rs.getDouble("weight")
                ));
            }
        }
        return entries;
    }
    
    public WeightEntry getCurrentWeightByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM WeightEntry WHERE user_id = ? ORDER BY date DESC LIMIT 1";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new WeightEntry(
                    rs.getInt("user_id"),
                    rs.getDate("date"),
                    rs.getDouble("weight")
                );
            }
        }
        return null;
    }
    
    public List<WeightEntry> getWeightEntriesLast7Days(int userId) throws SQLException {
        List<WeightEntry> entries = new ArrayList<>();
        String query = "SELECT * FROM WeightEntry WHERE user_id = ? AND date >= CURDATE() - INTERVAL 7 DAY";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                entries.add(new WeightEntry(
                    rs.getInt("user_id"),
                    rs.getDate("date"),
                    rs.getDouble("weight")
                ));
            }
        }
        return entries;
    }
}
