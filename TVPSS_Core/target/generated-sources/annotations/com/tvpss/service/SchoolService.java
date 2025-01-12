package com.tvpss.service;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.sql.Statement;

import javax.sql.rowset.serial.SerialBlob;
import org.springframework.stereotype.Service;

import com.mysql.cj.jdbc.Blob;
import com.tvpss.model.CrewModel;
import com.tvpss.model.School;
import com.tvpss.model.UserModel;

@Service
public class SchoolService {
	
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
    
    public static School addSchool(School school, int userId) {
        String insertQuery = "INSERT INTO school (name, fullAddress, state, districtID, contactNo, tvpssVersion) VALUES (?, ?, ?, ?, ?, ?)";
        String updateUserQuery = "UPDATE user SET districtID = ?, schoolID = ? WHERE id = ?"; // Update user with new districtID and schoolID

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            // Insert the new school
            stmt.setString(1, school.getName());
            stmt.setString(2, school.getFullAddress());
            stmt.setString(3, school.getState());
            stmt.setInt(4, school.getDistrictID());
            stmt.setString(5, school.getContactNo());
            stmt.setInt(6, 1); // Set tvpssVersion to 1

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int schoolID = generatedKeys.getInt(1); // Get the generated schoolID
                        school.setSchoolID(schoolID); // Set the generated schoolID

                        // Now insert a new studio with the same studioID as schoolID
                        addNewStudio(schoolID); // Call the method to add a new studio

                        // Update the user table with the new districtID and schoolID
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateUserQuery)) {
                            updateStmt.setInt(1, school.getDistrictID()); // Set the new districtID
                            updateStmt.setInt(2, schoolID); // Set the new schoolID
                            updateStmt.setInt(3, userId); // Set the specific user's ID

                            int updatedRows = updateStmt.executeUpdate();
                            if (updatedRows > 0) {
                                System.out.println("User  table updated successfully.");
                            } else {
                                System.out.println("No user found with the given ID.");
                            }
                        }
                    } else {
                        throw new SQLException("Creating school failed, no ID obtained.");
                    }
                }
                return school; // Return the school object with the generated ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Indicate failure
    }

    public static void addNewStudio(int schoolID) {
        String insertStudioQuery = "INSERT INTO studio (studioID, studioLevel, studioLevelStatus) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertStudioQuery)) {

            stmt.setInt(1, schoolID); // Set studioID to the same as schoolID
            stmt.setInt(2, 0); // Set default studioLevel to 0
            stmt.setString(3, "Pending"); // Set default studioLevelStatus

            stmt.executeUpdate(); // Execute the insert statement
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /*public static void addSchool(School school) {
        Connection conn = null;
        PreparedStatement insertSchoolStmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            conn.setAutoCommit(false);

            // Prepare the insert statement for the school
            String insertSchoolQuery = ("UPDATE school SET name = ?, fullAddress = ?, state = ?, WHERE schoolID = ?");
            insertSchoolStmt = conn.prepareStatement(insertSchoolQuery, Statement.RETURN_GENERATED_KEYS);

            // Set parameters for the school
            insertSchoolStmt.setString(1, school.getName());
            insertSchoolStmt.setString(2, school.getFullAddress());
            insertSchoolStmt.setString(3, school.getState());
            //insertSchoolStmt.setInt(4, school.getDistrictID());
            //insertSchoolStmt.setString(5, school.getContactNo());

            // Handle logo upload
            //if (school.getLogo() != null) {
             //   insertSchoolStmt.setBytes(6, school.getLogo());
            //} else {
             //   insertSchoolStmt.setNull(6, Types.BLOB); // Set logo to NULL if not provided
          //  }

            // Execute the insert statement
            int rowsAffected = insertSchoolStmt.executeUpdate();

            // Check if the school was added successfully
            if (rowsAffected > 0) {
                // Retrieve the generated schoolID
                try (ResultSet generatedKeys = insertSchoolStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        school.setSchoolID(generatedKeys.getInt(1)); // Set the generated schoolID
                    } else {
                        throw new SQLException("Creating school failed, no ID obtained.");
                    }
                }
            } else {
                throw new SQLException("Creating school failed, no rows affected.");
            }

            // Commit transaction
            conn.commit();

        } catch (SQLException e) {
            // Rollback on error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Resource cleanup
            try {
                if (insertSchoolStmt != null) insertSchoolStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }*/
    
 // Method to get schoolID by userID
    private int getSchoolIDByUserID(String userID) {
        String query = "SELECT schoolID FROM user WHERE userID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("schoolID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if no schoolID is found
    }
    
    public static School getSchoolDetailsByUserID(String userID) {
        String query = "SELECT s.* FROM school s " +
                       "JOIN user u ON s.schoolID = u.schoolID " +
                       "WHERE u.id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(6, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Construct and return a School object based on the result set
                    School school = new School();
                    school.setSchoolID(rs.getInt("schoolID"));
                    school.setName(rs.getString("name"));
                    school.setState(rs.getString("state"));
                    school.setFullAddress(rs.getString("fullAddress"));
                    school.setContactNo(rs.getString("contactNo"));
                    school.setVersionImageURL(rs.getString("versionImageURL"));
                    school.setDistrictID(rs.getInt("districtID"));
                    school.setTvpssVersion(rs.getInt("tvpssVersion"));
                    
                    return school;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no school is found for the given userID
    }
    
    public static UserModel getTeacherBySchoolId(int schoolId) {
        // Comprehensive role variations
        String[] potentialRoles = {
            "Teacher", "teacher", "TEACHER"
        };

        for (String role : potentialRoles) {
            String query = "SELECT u.* FROM user u " +
                           "WHERE u.schoolID = ? AND LOWER(u.role) = LOWER(?)";

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, schoolId);
                stmt.setString(2, role);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        UserModel teacher = new UserModel(rs);
                        System.out.println("Teacher found: " + teacher.getName());
                        return teacher;
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error querying teacher for schoolId " + schoolId);
                e.printStackTrace();
            }
        }

        // Last resort: print all users for this school
        checkAllUsersForSchool(schoolId);
        
        return null;
    }

    private static void checkAllUsersForSchool(int schoolId) {
        String query = "SELECT userID, name, role FROM user WHERE schoolID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, schoolId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("All users for schoolId " + schoolId + ":");
                while (rs.next()) {
                    System.out.println(
                        "UserID: " + rs.getInt("userID") + 
                        ", Name: " + rs.getString("name") + 
                        ", Role: " + rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static School getSchoolDetailsBySchoolID(int schoolID) {
        String query = "SELECT * FROM school WHERE schoolID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, schoolID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    School school = new School();
                    school.setSchoolID(rs.getInt("schoolID"));
                    school.setName(rs.getString("name"));
                    school.setState(rs.getString("state"));
                    school.setFullAddress(rs.getString("fullAddress"));
                    school.setContactNo(rs.getString("contactNo"));
                    school.setVersionImageURL(rs.getString("versionImageURL"));
                    school.setDistrictID(rs.getInt("districtID"));
                    school.setTvpssVersion(rs.getInt("tvpssVersion"));
                    school.setStudioID(rs.getInt("studioID"));
                    // Add any other fields you need
                    return school;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no school is found for the given schoolID
    }
    
    public static boolean editSchool(School school) {
        String updateQuery = "UPDATE school SET name = ?, fullAddress = ?, state = ?, districtID = ?, contactNo = ?, versionImageURL = ? WHERE schoolID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
        	System.out.println("Setting Prepared Statement Values:");
        	System.out.println("1: school.getName() = " + school.getName());
        	System.out.println("2: school.getFullAddress() = " + school.getFullAddress());
        	System.out.println("3: school.getState() = " + school.getState());
        	System.out.println("4: school.getDistrictID() = " + school.getDistrictID());
        	System.out.println("5: school.getContactNo() = " + school.getContactNo());
        	System.out.println("6: school.getVersionImageURL() = " + school.getVersionImageURL());
        	System.out.println("7: school.getSchoolID() = " + school.getSchoolID());

            stmt.setString(1, school.getName());
            stmt.setString(2, school.getFullAddress());
            stmt.setString(3, school.getState());
            stmt.setInt(4, school.getDistrictID());
            stmt.setString(5, school.getContactNo());
            stmt.setString(6, school.getVersionImageURL());
            stmt.setInt(7, school.getSchoolID()); // Set the schoolID for the WHERE clause

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0; // Return true if the update was successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Indicate failure
    }
    
    public static Map<String, Object> getSchoolsByDistrictId(int districtId) {
        Map<String, Object> result = new HashMap<>();
        List<School> schools = new ArrayList<>();
        List<UserModel> teachers = new ArrayList<>();

        String query = "SELECT s.schoolID, " +
                       "s.name AS schoolName, " +
                       "s.fullAddress, " +
                       "s.contactNo AS schoolContactNo, " +
                       "s.versionImageURL, " +
                       "s.districtID AS schoolDistrictID, " +
                       "s.tvpssVersion, " +
                       "d.name AS districtName, " +
                       "t.name AS teacherName, " +
                       "st.studioID AS studioID, " +
                       "st.studioLevel AS studioLevel " +
                       "FROM school s " +
                       "LEFT JOIN district d ON s.districtID = d.districtID " +
                       "LEFT JOIN user t ON s.schoolID = t.schoolID AND t.role = 'teacher' " + // Filter for teachers here
                       "LEFT JOIN studio st ON s.studioID = st.studioID " +
                       "WHERE s.districtID = ?"; // Only filter by district ID

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, districtId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Populate School object
                    School school = new School();
                    school.setSchoolID(rs.getInt("schoolID"));
                    school.setName(rs.getString("schoolName"));
                    school.setFullAddress(rs.getString("fullAddress"));
                    school.setContactNo(rs.getString("schoolContactNo"));
                    school.setVersionImageURL(rs.getString("versionImageURL"));
                    school.setDistrictID(rs.getInt("schoolDistrictID"));
                    school.setTvpssVersion(rs.getInt("tvpssVersion"));
                    school.setStudioID(rs.getInt("studioID")); // Assuming you have a setter for studio level

                    // Add school to list
                    schools.add(school);

                    // Populate UserModel object for teacher
                    if (rs.getString("teacherName") != null) { // Ensure there's user data
                        UserModel teacher = new UserModel();
                        teacher.setName(rs.getString("teacherName"));
                        teachers.add(teacher);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add lists to the result map
        result.put("schools", schools);
        result.put("teachers", teachers);

        return result;
    }
    
    public School getSchoolByName(String name) {
        String query = "SELECT * FROM school WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int schoolID = rs.getInt("schoolID");
                //String name = rs.getString("name");
                String state = rs.getString("state");
                String fullAddress = rs.getString("fullAddress");
                String contactNo = rs.getString("contactNo");
                String versionImageURL = rs.getString("versionImageURL");
                int districtID = rs.getInt("districtID");
                int tvpssVersion = rs.getInt("tvpssVersion");
                int studioID = rs.getInt("studioID");

                School school = new School(schoolID, name, null, state, fullAddress, contactNo, versionImageURL, districtID, tvpssVersion, studioID);
                return school;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no school was found with the given name
    }
    
    public static List<Map<String, Object>> getAllEquipment() {
        List<Map<String, Object>> equipmentList = new ArrayList<>();
        String query = "SELECT equipID, equipName FROM equipment";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> equipment = new HashMap<>();
                equipment.put("equipID", rs.getInt("equipID"));
                equipment.put("equipName", rs.getString("equipName"));
                equipmentList.add(equipment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return equipmentList;
    }
    
    public static void editEquipment(int schoolID, Map<String, String> equipmentStatus, String imagesLink) {
        // Retrieve studioID
        int studioID = getStudioIDBySchoolID(schoolID);
        if (studioID == 0) {
            System.out.println("No studio found for the given schoolID.");
            return;
        }

        // Queries for updating equipment and studio status
        String updateExistingEquipQuery = "UPDATE studio_equip " +
            "SET availability = ? " +
            "WHERE studioID = ? AND equipID = (SELECT equipID FROM equipment WHERE equipName = ?)";
        
        String updateStudioQuery = "UPDATE studio " +
            "SET imagesLink = ?, studioLevelStatus = 'Pending' " + // ALWAYS set to Pending
            "WHERE studioID = ?";

        Connection conn = null;
        PreparedStatement updateEquipStmt = null;
        PreparedStatement updateStudioStmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            conn.setAutoCommit(false);

            // Prepare statements
            updateEquipStmt = conn.prepareStatement(updateExistingEquipQuery);
            updateStudioStmt = conn.prepareStatement(updateStudioQuery);

            // Process each equipment status
            for (Map.Entry<String, String> entry : equipmentStatus.entrySet()) {
                String equipmentName = entry.getKey();
                String availability = entry.getValue();

                // Update existing equipment
                updateEquipStmt.setString(1, availability);
                updateEquipStmt.setInt(2, studioID);
                updateEquipStmt.setString(3, equipmentName);
                updateEquipStmt.executeUpdate();
            }

            // Update studio images link and ALWAYS set status to Pending
            updateStudioStmt.setString(1, imagesLink);
            updateStudioStmt.setInt(2, studioID);
            updateStudioStmt.executeUpdate();

            // Commit transaction
            conn.commit();

        } catch (SQLException e) {
            // Rollback on error
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction rolled back due to error");
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Resource cleanup
            try {
                if (updateEquipStmt != null) updateEquipStmt.close();
                if (updateStudioStmt != null) updateStudioStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static int getEquipIDByName(String equipmentName) {
        String query = "SELECT equipID FROM equipment WHERE equipName = ?";
        int equipID = 0; // Default value if not found

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, equipmentName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                equipID = rs.getInt("equipID"); // Retrieve the equipID from the result set
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return equipID; // Return the found equipID or 0 if not found
    }
    
    public static void addNewEquipment(int schoolID, Map<String, String> equipmentStatus, String imagesLink) {
        Connection conn = null;
        PreparedStatement insertStudioStmt = null;
        PreparedStatement insertEquipStmt = null;
        PreparedStatement updateSchoolStmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            conn.setAutoCommit(false);

            // First, check if studio exists
            int studioID = getStudioIDBySchoolID(schoolID);
            
            // If no studio exists, create a new studio
            if (studioID == 0) {
                // Insert new studio
                String insertStudioQuery = "INSERT INTO studio (studioLevel, studioLevelStatus, imagesLink) VALUES (?, ?, ?)";
                insertStudioStmt = conn.prepareStatement(insertStudioQuery, Statement.RETURN_GENERATED_KEYS);
                
                // Initially set studio level to 0 and status to Pending
                insertStudioStmt.setInt(1, 0);
                insertStudioStmt.setString(2, "Pending");
                insertStudioStmt.setString(3, imagesLink);
                insertStudioStmt.executeUpdate();

                // Retrieve the new studioID
                try (ResultSet generatedKeys = insertStudioStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        studioID = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating studio failed, no ID obtained.");
                    }
                }

                // Update school with new studioID
                String updateSchoolQuery = "UPDATE school SET studioID = ? WHERE schoolID = ?";
                updateSchoolStmt = conn.prepareStatement(updateSchoolQuery);
                updateSchoolStmt.setInt(1, studioID);
                updateSchoolStmt.setInt(2, schoolID);
                updateSchoolStmt.executeUpdate();
            }

            // Prepare equipment insert statement
            String insertEquipQuery = "INSERT INTO studio_equip (studioID, equipID, availability) " +
                "SELECT ?, equipID, ? FROM equipment WHERE equipName = ?";
            insertEquipStmt = conn.prepareStatement(insertEquipQuery);

            // Insert equipment
            for (Map.Entry<String, String> entry : equipmentStatus.entrySet()) {
                String equipmentName = entry.getKey();
                String availability = entry.getValue();

                insertEquipStmt.setInt(1, studioID);
                insertEquipStmt.setString(2, availability);
                insertEquipStmt.setString(3, equipmentName);
                insertEquipStmt.executeUpdate();
            }

            // Commit transaction
            conn.commit();

        } catch (SQLException e) {
            // Rollback on error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Resource cleanup
            try {
                if (insertStudioStmt != null) insertStudioStmt.close();
                if (insertEquipStmt != null) insertEquipStmt.close();
                if (updateSchoolStmt != null) updateSchoolStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

   
    // Helper method to get studioID
    private static int getStudioIDBySchoolID(int schoolID) {
        String query = "SELECT studioID FROM school WHERE schoolID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, schoolID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("studioID");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving studioID: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    public static void updateStudioLevel(int schoolID) {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            
            // First, get the studioID
            int studioID = getStudioIDBySchoolID(schoolID);
            if (studioID == 0) {
                System.out.println("No studio found for school ID: " + schoolID);
                return;
            }

            // Retrieve studio level status
            String statusQuery = "SELECT studioLevelStatus FROM studio WHERE studioID = ?";
            String studioLevelStatus;
            
            try (PreparedStatement statusStmt = conn.prepareStatement(statusQuery)) {
                statusStmt.setInt(1, studioID);
                try (ResultSet rs = statusStmt.executeQuery()) {
                    if (rs.next()) {
                        studioLevelStatus = rs.getString("studioLevelStatus");
                    } else {
                        System.out.println("No studio status found");
                        return;
                    }
                }
            }

            // Retrieve 'New' equipment details
            String selectNewEquipQuery = "SELECT e.equipName, se.equipID " +
                "FROM studio_equip se " +
                "JOIN equipment e ON se.equipID = e.equipID " +
                "WHERE se.studioID = ? AND se.availability = 'New'";
            
            List<Map<String, Object>> newEquipment = new ArrayList<>();
            
            try (PreparedStatement selectStmt = conn.prepareStatement(selectNewEquipQuery)) {
                selectStmt.setInt(1, studioID);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> equip = new HashMap<>();
                        equip.put("equipName", rs.getString("equipName"));
                        equip.put("equipID", rs.getInt("equipID"));
                        newEquipment.add(equip);
                    }
                }
            }

            // Update each 'New' equipment individually
            for (Map<String, Object> equip : newEquipment) {
                String updateEquipQuery = "UPDATE studio_equip " +
                    "SET availability = ? " +
                    "WHERE studioID = ? AND equipID = ? AND availability = 'New'";
                
                try (PreparedStatement updateStmt = conn.prepareStatement(updateEquipQuery)) {
                    if ("Rejected".equals(studioLevelStatus)) {
                        updateStmt.setString(1, "No");
                    } else if ("Approved".equals(studioLevelStatus)) {
                        updateStmt.setString(1, "Yes");
                    } else {
                        continue;
                    }
                    
                    updateStmt.setInt(2, studioID);
                    updateStmt.setInt(3, (Integer)equip.get("equipID"));
                    
                    int rowsUpdated = updateStmt.executeUpdate();
                    System.out.println("Updated " + equip.get("equipName") + 
                        " to " + (rowsUpdated > 0 ? "New Status" : "Unchanged"));
                }
            }

            // Determine and update studio level
            Map<String, String> equipmentStatus = getEquipmentStatus(studioID);
            int newStudioLevel = determineStudioLevel(equipmentStatus);
            
            // Update studio level and status
            String updateStudioQuery = "UPDATE studio " +
                "SET studioLevel = ?, " +
                "studioLevelStatus = 'Approved' " +
                "WHERE studioID = ?";
            
            try (PreparedStatement updateStmt = conn.prepareStatement(updateStudioQuery)) {
                updateStmt.setInt(1, newStudioLevel);
                updateStmt.setInt(2, studioID);
                
                int rowsUpdated = updateStmt.executeUpdate();
                
                System.out.println("Studio Level Update Details:");
                System.out.println("School ID: " + schoolID);
                System.out.println("Studio ID: " + studioID);
                System.out.println("New Studio Level: " + newStudioLevel);
                System.out.println("Rows Updated: " + rowsUpdated);
            }

        } catch (SQLException e) {
            System.err.println("Error in updateStudioLevel:");
            System.err.println("School ID: " + schoolID);
            System.err.println("Error Message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to update specific equipment availability
    private static void updateEquipmentAvailability(int studioID, String equipmentName, String newAvailability) {
        String updateQuery = "UPDATE studio_equip se " +
            "JOIN equipment e ON se.equipID = e.equipID " +
            "SET se.availability = ? " +
            "WHERE se.studioID = ? AND e.equipName = ? AND se.availability = 'New'";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, newAvailability);
            stmt.setInt(2, studioID);
            stmt.setString(3, equipmentName);
            
            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Updated " + equipmentName + " to " + newAvailability + ": " + rowsUpdated + " rows");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get current equipment status
    private static Map<String, String> getEquipmentStatus(int studioID) {
        Map<String, String> equipmentStatus = new HashMap<>();
        
        String query = "SELECT e.equipName, se.availability " +
            "FROM studio_equip se " +
            "JOIN equipment e ON se.equipID = e.equipID " +
            "WHERE se.studioID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studioID);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    equipmentStatus.put(
                        rs.getString("equipName"), 
                        rs.getString("availability")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return equipmentStatus;
    }

    // Level determination method with additional logging
    private static int determineStudioLevel(Map<String, String> equipmentStatus) {
        System.out.println("Determining Studio Level. Total Equipment: " + equipmentStatus.size());
        
        // Print all equipment statuses
        equipmentStatus.forEach((name, status) -> 
            System.out.println("Equipment: " + name + ", Status: " + status)
        );

        // Level 3 Condition
        boolean isLevel3 = hasAllEquipment(equipmentStatus, 
            "TV Program/Show Corner",
            "Editing Corner",
            "External Mic",
            "Tripod",
            "Mobile Lighting (3 Point)",
            "Camera",
            "Editing Software",
            "Permanent Green Screen");

        if (isLevel3) {
            System.out.println("Meets Level 3 Criteria");
            return 3;
        }

        // Level 2 Condition
        boolean isLevel2 = hasAllEquipment(equipmentStatus, 
            "TV Program/Show Corner",
            "Editing Corner",
            "External Mic",
            "Green Screen Set",
            "Tripod",
            "Mobile Lighting",
            "Webcam",
            "Editing Software");

        if (isLevel2) {
            System.out.println("Meets Level 2 Criteria");
            return 2;
        }

        // Level 1 Condition
        boolean isLevel1 = hasAllEquipment(equipmentStatus, 
            "TV Program/Show Corner",
            "Smartphone",
            "External Mic",
            "Monopod",
            "Ring Light");

        if (isLevel1) {
            System.out.println("Meets Level 1 Criteria");
            return 1;
        }

        System.out.println("No Level Criteria Met");
        return 0;
    }

    // Helper method with detailed logging
    private static boolean hasAllEquipment(Map<String, String> equipmentStatus, String... requiredEquipment) {
        for (String equipment : requiredEquipment) {
            if (!equipmentStatus.containsKey(equipment) || 
                !"Yes".equals(equipmentStatus.get(equipment))) {
                System.out.println("Missing or Not Available: " + equipment);
                return false;
            }
        }
        return true;
    }

    public static void updateStudioLevelStatus(int schoolID, String newStatus) {
        String query = "UPDATE studio SET studioLevelStatus = ? WHERE studioID = (SELECT studioID FROM school WHERE schoolID = ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, schoolID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> getSchoolsStudio(int districtId) {
        Map<Integer, Map<String, Object>> schoolMap = new HashMap<>();

        String query = "SELECT " +
            "s.schoolID, " +
            "s.name AS schoolName, " +
            "st.studioLevel, " +
            "COUNT(DISTINCT CASE WHEN se.availability = 'Yes' THEN se.equipID END) AS availableEquipmentCount " +
            "FROM school s " +
            "LEFT JOIN studio st ON s.studioID = st.studioID " +
            "LEFT JOIN studio_equip se ON st.studioID = se.studioID " +
            "WHERE s.districtID = ? " +
            "GROUP BY s.schoolID, s.name, st.studioLevel";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, districtId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int schoolID = rs.getInt("schoolID");
                    String schoolName = rs.getString("schoolName");
                    int studioLevel = rs.getInt("studioLevel");
                    int availableEquipmentCount = rs.getInt("availableEquipmentCount");

                    Map<String, Object> schoolInfo = new HashMap<>();
                    schoolInfo.put("schoolID", schoolID);
                    schoolInfo.put("schoolName", schoolName);
                    schoolInfo.put("studioLevel", studioLevel);
                    schoolInfo.put("availableEquipmentCount", availableEquipmentCount);

                    schoolMap.put(schoolID, schoolInfo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("schoolDetails", new ArrayList<>(schoolMap.values()));
        return result;
    }
    
    public static Map<String, Object> getStudioAndEquipmentDetails(int schoolID) {
        Map<String, Object> result = new HashMap<>();
        List<Object[]> equipmentList = new ArrayList<>();
        int studioLevel = 0;
        String imagesLink = null;
        String studioLevelStatus = null;

        // Adjust the query to get the studio level and equipment based on the studioID
        String query = "SELECT st.studioLevel, st.imagesLink, st.studioLevelStatus, e.equipName, se.availability " +
		                "FROM studio st " +
		                "LEFT JOIN studio_equip se ON st.studioID = se.studioID " +
		                "LEFT JOIN equipment e ON se.equipID = e.equipID " +
		                "WHERE st.studioID IN (SELECT studioID FROM school WHERE schoolID = ?)"; //Subquery to get studioID for the given schoolID

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, schoolID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    studioLevel = rs.getInt("studioLevel");
                    imagesLink = rs.getString("imagesLink");
                    studioLevelStatus = rs.getString("studioLevelStatus");
                    System.out.println("Studio Level Status: " + studioLevelStatus);
                    do {
                        Object[] equipment = new Object[2]; // Array to hold equipment name and status
                        equipment[0] = rs.getString("equipName"); // Equipment name
                        equipment[1] = rs.getString("availability"); // Availability status
                        equipmentList.add(equipment);
                    } while (rs.next());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Images Linkk: " + imagesLink); // Debugging line
        result.put("studioLevel", studioLevel);
        result.put("studioLevelStatus", studioLevelStatus);
        result.put("equipmentList", equipmentList);
        result.put("imagesLink", imagesLink);
        return result;
    }
    
    public static List<Map<String, Object>> getDistrictsWithDetails() {
        List<Map<String, Object>> districtDetails = new ArrayList<>();
        String sql = "SELECT d.districtID, d.name AS districtName, " +
                     "COALESCE(u.name, 'N/A') AS personInCharge," +
                     "(SELECT COUNT(*) FROM school s WHERE s.districtID = d.districtID) AS totalSchools " +
                     "FROM district d " +
                     "LEFT JOIN user u ON d.districtID = u.districtID AND u.role = 'district officer'";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> districtInfo = new HashMap<>();
                districtInfo.put("districtID", rs.getInt("districtID"));
                districtInfo.put("districtName", rs.getString("districtName"));
                districtInfo.put("personInCharge", rs.getString("personInCharge"));
                districtInfo.put("totalSchools", rs.getInt("totalSchools"));
                districtDetails.add(districtInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return districtDetails;
    }
    
    public static Map<Integer, Integer> getAllStudios() {
        Map<Integer, Integer> studios = new LinkedHashMap<>();
        String sql = "SELECT studioID, studioLevel FROM studio";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int studioID = rs.getInt("studioID");
                int studioLevel = rs.getInt("studioLevel");
                studios.put(studioID, studioLevel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studios;
    }
    
    public static Map<Integer, String> getAllDistricts() {
        Map<Integer, String> districts = new LinkedHashMap<>();
        String sql = "SELECT districtID, name FROM district";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int districtID = rs.getInt("districtID");
                String districtName = rs.getString("name");
                districts.put(districtID, districtName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return districts;
    }
    
    public static String convertBlobToBase64(byte[] logo) {
        return Base64.getEncoder().encodeToString(logo);
    }
}
