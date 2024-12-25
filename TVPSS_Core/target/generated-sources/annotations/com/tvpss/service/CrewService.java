package com.tvpss.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tvpss.model.CrewModel;
import com.tvpss.model.UserModel;

public class CrewService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/tvpssdb";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    // Load the MySQL JDBC Driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static UserModel getUserDetails(String userID) {
        String query = "SELECT * FROM user WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UserModel(
                        rs.getInt("id"),
                        rs.getString("userID"),
                        rs.getString("name"),
                        rs.getString("contactNo"),
                        rs.getString("email"),
                        rs.getString("status"),
                        rs.getString("role"),
                        rs.getString("password"),
                        rs.getTimestamp("lastActive"),
                        rs.getString("session"),
                        rs.getInt("districtID"),
                        rs.getInt("schoolID")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<CrewModel> getCrewDetails(String userID) {
        List<CrewModel> crewList = new ArrayList<>();
        String query = "SELECT * FROM tvpss_crew_info WHERE userID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    crewList.add(new CrewModel(
                        rs.getInt("crewID"),
                        rs.getString("abilities"),
                        rs.getString("status"),
                        rs.getString("session"),
                        rs.getString("className"),
                        rs.getInt("schoolID"),
                        rs.getString("userID")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return crewList;
    }

}
