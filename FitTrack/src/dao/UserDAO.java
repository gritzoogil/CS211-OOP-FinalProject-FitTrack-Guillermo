package dao;

import model.User;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
	
	// add user method
	public static void addUser(User user) throws SQLException {
	    String query = "INSERT INTO User (name, sex, birth_year, height, weight, start_date, end_date, goal) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	         
	        stmt.setString(1, user.getName());
	        stmt.setString(2, user.getSex());
	        stmt.setInt(3, user.getBirthYear());
	        stmt.setDouble(4, user.getHeight());
	        stmt.setDouble(5, user.getWeight());
	        stmt.setDate(6, java.sql.Date.valueOf(user.getStartDate()));
	        stmt.setDate(7, user.getEndDate() != null ? new java.sql.Date(user.getEndDate().getTime()) : null);
	        stmt.setString(8, user.getGoal());

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
    
    public User getUserByName(String name) {
        String query = "SELECT * FROM User WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("birthYear"),
                    rs.getString("sex"),
                    rs.getDouble("height"),
                    rs.getDouble("weight"),
                    rs.getDate("startDate").toLocalDate(),
                    rs.getDate("endDate"),
                    rs.getString("goal")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user: " + e.getMessage());
            e.printStackTrace();
        }
        return null; 
    }
    
    public int getUserIdByName(String name) {
        String query = "SELECT id FROM User WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                System.out.println("User not found.");
                return -1; 
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user ID: " + e.getMessage());
            e.printStackTrace();
            return -1; 
        }
    }
}
