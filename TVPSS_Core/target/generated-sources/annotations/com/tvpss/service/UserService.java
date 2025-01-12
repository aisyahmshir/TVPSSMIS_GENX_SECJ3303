package com.tvpss.service;
import org.springframework.security.crypto.bcrypt.BCrypt;

import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // For password encoding/decoding
import org.springframework.security.crypto.password.PasswordEncoder; 

import java.sql.Statement;

import com.tvpss.model.School;
import com.tvpss.model.UserModel;


@Service
public class UserService {
	

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

    // Method to check if the email already exists in the database
    public static boolean isEmailExist(String email) {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;  // If count > 0, email exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // If query fails or email doesn't exist
    }

    // Method to check if the contact number already exists in the database
    public static boolean isContactNoExist(String contactNo) {
        String query = "SELECT COUNT(*) FROM user WHERE contactNo = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, contactNo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;  // If count > 0, contact number exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // If query fails or contact number doesn't exist
    }

    // Method to register a new user, accepting districtID and schoolID as parameters
    public static boolean registerUser(String userID, String name, String contactNo, String email, String password, String role, String status, long schoolID, long districtID) {
    	String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    	String insertQuery = "INSERT INTO user (userID, name, contactNo, email, password, role, status, schoolID, districtID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

        	stmt.setString(1, userID);
            stmt.setString(2, name);
            stmt.setString(3, contactNo);
            stmt.setString(4, email);
            stmt.setString(5, hashedPassword);  // Assuming password is already hashed before passing here
            stmt.setString(6, role);
            stmt.setString(7, status);
            stmt.setLong(8, schoolID);
            stmt.setLong(9, districtID);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;  // Returns true if insertion was successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Returns false if there was an error during the insertion
    }
    
//    public UserModel validateLogin(String email, String password) {
//    	String query = "SELECT id, userID, role, status FROM user WHERE email = ? AND password = ?";
//        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            stmt.setString(1, email);
//            stmt.setString(2, password); // Assuming password is hashed before saving
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    String status = rs.getString("status");
//                    if ("Approved".equalsIgnoreCase(status)) {
//                    	UserModel user = new UserModel();
//                    	user.setId(rs.getInt("id"));
//                        user.setUserID(rs.getString("userID"));
//                        user.setRole(rs.getString("role"));
//                        user.setStatus(status);
//                        return user; 
//                    } else {
//                        return null; // Account exists but is not approved
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null; // Invalid email/password combination
//    }
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    public UserModel validateLogin(String email, String password) {
        String query = "SELECT id, userID, role, status, password FROM user WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email); // Set email to find the user in the database

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password"); // Fetch the stored hashed password
                    String status = rs.getString("status");

                    // Use bcrypt to compare the provided password with the stored hashed password
                    if (passwordEncoder.matches(password, hashedPassword)) {
                        //If password matches, check if the account is approved
                        if ("Approved".equalsIgnoreCase(status)) {
                            UserModel user = new UserModel();
                            user.setId(rs.getInt("id"));
                            user.setUserID(rs.getString("userID"));
                            user.setRole(rs.getString("role"));
                            user.setStatus(status);
                            return user; // Return the user details if password is valid and account is approved
                        } else {
                            return null; // Account exists but is not approved
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Invalid email/password combination or database error
    }

    
    public UserModel getLoggedInUser(HttpSession session) {
	    // Assuming the user is stored in the session with the attribute "loggedInUser"
	    UserModel loggedInUser = (UserModel) session.getAttribute("loggedInUser");
	    if (loggedInUser != null) {
	    	System.out.println("Logged-in User ID: " + loggedInUser.getId()); // Check if the id is being fetched correctly
	        return loggedInUser; // Return the logged-in user object
	    } else {
	        return null; // Return null if no user is found in the session
	    }
	}

    public List<UserModel> getAllUsers() {
        List<UserModel> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                UserModel user = new UserModel();
                user.setUserID(rs.getString("userID"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setLastActive(rs.getTimestamp("lastActive"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public boolean deleteUser(String userID) {
        String deleteQuery = "DELETE FROM user WHERE userID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            
            stmt.setString(1, userID);
            int rowsDeleted = stmt.executeUpdate();
            
            return rowsDeleted > 0;  // Returns true if deletion was successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Returns false if there was an error during deletion
    }
    
 // Fetch users with "Pending" status
    public List<UserModel> getPendingUsers() {
        List<UserModel> users = new ArrayList<>();
        String query = "SELECT * FROM user WHERE status = 'Pending'";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                UserModel user = new UserModel();
                user.setUserID(rs.getString("userID"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setContactNo(rs.getString("contactNo"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public List<UserModel> getApprovedUsers() {
        List<UserModel> users = new ArrayList<>();
        String query = "SELECT * FROM user WHERE status = 'Approved'";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                UserModel user = new UserModel();
                user.setUserID(rs.getString("userID"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setContactNo(rs.getString("contactNo"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Update user status
    public boolean updateUserStatus(String userID, String status) {
        String updateQuery = "UPDATE user SET status = ? WHERE userID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            
            stmt.setString(1, status);
            stmt.setString(2, userID);
            int rowsUpdated = stmt.executeUpdate();
            
            return rowsUpdated > 0;  // Returns true if update was successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Returns false if there was an error during the update
    }

    public UserModel findByUserId(String userID) {
        String query = "SELECT * FROM user WHERE userID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userID);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Create a UserModel object and populate it with the result set data
                    UserModel user = new UserModel();
                    user.setUserID(rs.getString("userID"));
                    user.setName(rs.getString("name"));
                    user.setContactNo(rs.getString("contactNo"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));
                    //user.setStatus(rs.getString("password"));
                    //user.setLastActive(rs.getTimestamp("lastActive"));
                    
                    return user; // Return the populated UserModel object
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null; // Return null if user is not found
    }
    
    public boolean updateUserDetails(UserModel user) {
        String query = "UPDATE user SET name = ?, contactNo = ?, email = ? WHERE userID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameters for the prepared statement
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getContactNo());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getUserID());
            
            // Execute the update
            int rowsAffected = stmt.executeUpdate();
            
            // Return true if the update was successful
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Return false if the update fails
        return false;
    }

    
    public boolean updatePassword(String userID, String newPassword) {
        String query = "UPDATE user SET password = ? WHERE userID = ?";
        boolean isUpdated = false;

        // Hash the password before updating the database
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(newPassword);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters for the prepared statement
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, userID);

            // Execute the update query
            int rowsAffected = pstmt.executeUpdate();
            isUpdated = rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    }
    
}
