package dao;

import model.CalorieEntry;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalorieEntryDAO {

    public void addCalorieEntry(CalorieEntry entry) throws SQLException {
        String query = "INSERT INTO CalorieEntry (user_id, date, calories, proteins, carbs, fats) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, entry.getUserId());
            stmt.setDate(2, new java.sql.Date(entry.getDate().getTime()));
            stmt.setInt(3, entry.getCalories());
            stmt.setInt(4, entry.getProteins());
            stmt.setInt(5, entry.getCarbs());
            stmt.setInt(6, entry.getFats());
            stmt.executeUpdate();
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
                    rs.getDate("date"),
                    rs.getInt("calories"),
                    rs.getInt("proteins"),
                    rs.getInt("carbs"),
                    rs.getInt("fats")
                ));
            }
        }
        return entries;
    }
}
