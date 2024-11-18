package dao;

import model.User;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
	
	// add user method
    public static void addUser(User user) throws SQLException {
        String query = "INSERT INTO User (name, birth_year, start_date, end_date, goal) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
        	PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        	
        	// set name, birth_year, start_date, end_date
            stmt.setString(1, user.getName());
            stmt.setInt(2, user.getBirthYear());
            stmt.setDate(3, java.sql.Date.valueOf(user.getStartDate()));
            
            // ensure only date portion is stored for endDate
            if (user.getEndDate() != null) {
                stmt.setDate(4, new java.sql.Date(user.getEndDate().getTime()));
            } else {
                stmt.setDate(4, null);
            }
            
            // set goal
            stmt.setString(5, user.getGoal());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
        }
    }

    public User getUserById(int id) throws SQLException {
        String query = "SELECT * FROM User WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("birth_year"),
                    rs.getString("sex"),
                    rs.getDouble("height"),
                    rs.getDouble("weight"),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("end_date"),
                    rs.getString("goal")
                );
            }
        }
        return null;
    }
}
