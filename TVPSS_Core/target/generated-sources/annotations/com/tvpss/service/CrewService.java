package com.tvpss.service;

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

import com.tvpss.model.CrewModel;
import com.tvpss.model.School;
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

    public static List<CrewModel> getCrewDetailsWithTeachers(String userID) {
        List<CrewModel> crewList = new ArrayList<>();
        String query = 
        	    "SELECT " +
        	    "    c.crewID, " +
        	    "    c.abilities, " +
        	    "    c.status, " +
        	    "    c.session, " +
        	    "    c.className, " +
        	    "    c.schoolID, " +
        	    "    c.userID, " +
        	    "    u.id, " +
        	    "    u.name, " +
        	    "    u.email, " +
        	    "    u.role, " +
        	    "    u.schoolID, " +
        	    "    u.userID " +
        	    "FROM " +
        	    "    tvpss_crew_info c " +
        	    "JOIN " +
        	    "    user u ON c.picID = u.id " +
        	    "WHERE " +
        	    "    c.userID = ? AND u.role = 'teacher'";


        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userID);
            Map<Integer, CrewModel> crewMap = new HashMap<>();

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int crewID = rs.getInt("crewID");
                    CrewModel crew = crewMap.getOrDefault(crewID, new CrewModel(
                        rs.getInt("crewID"),
                        rs.getString("abilities"),
                        rs.getString("status"),
                        rs.getString("session"),
                        rs.getString("className"),
                        rs.getInt("schoolID"),
                        rs.getString("userID")
                    ));
                    System.out.println("crew"+crew);
                    UserModel teacher = new UserModel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getInt("schoolID"),
                        rs.getString("userID")
                    );
                    System.out.println("teacher"+teacher);
                    crew.addTeacher(teacher);
                    crewMap.put(crewID, crew);
                    
                }
            }

            crewList.addAll(crewMap.values());
            for (CrewModel crew : crewList) {
                System.out.println("hehe" +crew.toString());
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return crewList;
    }
    
    
    public static boolean insertApplication(String studentName, String email, String contactNo, String studentClass,
            String abilities, String session, String status, String userID, String schoolID) {
    		String getTeacherQuery = "SELECT id, name FROM user WHERE schoolID = ? AND role = 'teacher' LIMIT 1";
			String insertQuery = "INSERT INTO tvpss_crew_info (schoolID, userID, picID, className, abilities, session, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
			
			try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			// Step 1: Retrieve the teacher's picID
			int picID = -1;
			String teacherName = "";
			try (PreparedStatement getTeacherStmt = conn.prepareStatement(getTeacherQuery)) {
			getTeacherStmt.setString(1, schoolID);
			try (ResultSet rs = getTeacherStmt.executeQuery()) {
				if (rs.next()) {
				picID = rs.getInt("id");
				teacherName = rs.getString("name");
				}
			}
			}
			
			// Check if a teacher was found
			if (picID == -1) {
				System.out.println("No teacher found for the given schoolID.");
				return false;
			}
			
			// Step 2: Insert the application
			try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
				insertStmt.setString(1, schoolID);
				insertStmt.setString(2, userID);
				insertStmt.setInt(3, picID); // Assign the retrieved picID
				insertStmt.setString(4, studentClass);
				insertStmt.setString(5, abilities);
				insertStmt.setString(6, session);
				insertStmt.setString(7, status);
				
				int rowsInserted = insertStmt.executeUpdate();
				return rowsInserted > 0;
			}
			
			} catch (SQLException e) {
				e.printStackTrace();
			return false;
			}
}

// FOR TEACHER MODULE
    public static List<Map<String, Object>> getCrewsWithNamesBySchoolId(Long long1) {
        List<Map<String, Object>> crewWithNames = new ArrayList<>();
        String query = "SELECT * FROM tvpss_crew_info WHERE schoolID = ? AND status = 'Approved'";

        System.out.println("schoolID " + long1);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, long1);

            try (ResultSet rs = stmt.executeQuery()) {
                List<String> userIds = new ArrayList<>();
                Map<String, CrewModel> crewMap = new HashMap<>();

                // Fetch crew data
                while (rs.next()) {
                    CrewModel crew = new CrewModel(
                        rs.getInt("crewID"),
                        rs.getString("abilities"),
                        rs.getString("status"),
                        rs.getString("session"),
                        rs.getString("className"),
                        rs.getInt("schoolID"),
                        rs.getString("userID")
                    );
                    System.out.println("school class name " + crew.getClassName());
                    String userId = rs.getString("userID");
                    userIds.add(userId);

                    crewMap.put(userId, crew);
                }

                // Fetch names of associated users
                Map<String, String> userNames = getUserNamesByIds(userIds);

                // Combine data
                for (String userId : crewMap.keySet()) {

                    Map<String, Object> crewData = new HashMap<>();
                    CrewModel crew = crewMap.get(userId);
                	System.out.println("crew detailz "+ userNames);
                	System.out.println("crew detailz userID "+ userId);
                    crewData.put("crew", crew);
                    crewData.put("name", userNames.getOrDefault(userId, "Unknown"));

                    crewWithNames.add(crewData);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return crewWithNames;
    }

    private static Map<String, String> getUserNamesByIds(List<String> userIds) {
        Map<String, String> userNames = new HashMap<>();
        if (userIds.isEmpty()) return userNames;

        String query = "SELECT userID,id, name FROM user WHERE id IN (" +
                       String.join(",", Collections.nCopies(userIds.size(), "?")) + ")";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int i = 0; i < userIds.size(); i++) {
                stmt.setString(i + 1, userIds.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                	System.out.println("name is "+rs.getString("id"));
                    userNames.put(rs.getString("id"), rs.getString("name"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userNames;
    }
    
    public static UserModel getTeacherBySchoolId(Long long1) {
    	String query = "SELECT u.* FROM user u " +
                "WHERE u.schoolID = ? AND u.role = 'Teacher'";


        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, long1);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Assuming UserModel has a constructor that takes a ResultSet
                    return new UserModel(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no teacher is found
    }

 // Add the method to get school details by userID
    public static School getSchoolDetailsByUserID(String userID) {
        String query = "SELECT s.* FROM school s " +
                       "JOIN user u ON s.schoolID = u.schoolID " +
                       "WHERE u.id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Construct and return a School object based on the result set
                    School school = new School();
                    school.setSchoolID(rs.getLong("schoolID"));
                    school.setName(rs.getString("name"));
                    school.setState(rs.getString("state"));
                    school.setFullAddress(rs.getString("fullAddress"));
                    school.setContactNo(rs.getString("contactNo"));
                    school.setVersionImageURL(rs.getString("versionImageURL"));
                    school.setDistrictID(rs.getLong("districtID"));
                    school.setTvpssVersion(rs.getInt("tvpssVersion"));
                    
                    return school;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no school is found for the given userID
    }



}
