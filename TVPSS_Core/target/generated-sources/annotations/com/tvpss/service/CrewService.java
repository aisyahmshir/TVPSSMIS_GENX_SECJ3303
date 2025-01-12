package com.tvpss.service;

import java.io.UnsupportedEncodingException;
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

    public static UserModel getUserDetails(int userID) {
        String query = "SELECT * FROM user WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, userID);
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

    public static List<CrewModel> getCrewDetailsWithTeachers(int userID) {
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

            stmt.setInt(1, userID);
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
    public static List<Map<String, Object>> getCrewsWithNamesBySchoolId(int long1) {
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
    
    public static UserModel getTeacherBySchoolId(int long1) {
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
    public static School getSchoolDetailsByUserID(int userID) {
        String query = "SELECT s.* FROM school s " +
                       "JOIN user u ON s.schoolID = u.schoolID " +
                       "WHERE u.id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, userID);
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
    
    public static boolean submitTVPSSApplication(int schoolID, java.sql.Date dateApplied, String url, String status, int versionApplied) {
        String insertQuery = "INSERT INTO tvpssversionapplication(schoolID, dateApplied, url, versionApplied) " +
                             "VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setInt(1, schoolID);
            stmt.setDate(2, dateApplied);
            stmt.setString(3, url);
            stmt.setInt(4, versionApplied);

            int rowsAffected = stmt.executeUpdate(); // Execute the query and get affected rows
            System.out.println("Record inserted successfully into tvpssversionapplication table.");
            return rowsAffected > 0; // Return true if at least one row is affected
        } catch (SQLException e) {
            System.err.println("Error inserting record: " + e.getMessage());
            return false; // Return false if there's an exception
        }
    }
    
    public static boolean checkPendingApplication(int schoolID) {
        String query = "SELECT COUNT(*) FROM tvpssversionapplication WHERE schoolID = ? AND status = 'Pending'";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, schoolID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;  // If count is greater than 0, there is at least one pending application
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if no pending application is found or if an error occurs
    }

    
    public static List<Map<String, Object>> getTVPSSApplicationCrew(int id) {
        List<Map<String, Object>> crewDetails = new ArrayList<>();

        String getSchoolIDQuery = 
            "SELECT schoolID FROM user WHERE id = ?";
        String getPendingCrewQuery = 
            "SELECT c.crewID, c.abilities, c.status, c.session, c.className, c.schoolID, c.userID " +
            "FROM tvpss_crew_info c WHERE c.schoolID = ? AND c.status = 'Pending'";
        String getUserDetailsQuery = 
            "SELECT id, name, email, contactNo FROM user WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Step 1: Get schoolID
            String schoolID = null;
            try (PreparedStatement stmt = conn.prepareStatement(getSchoolIDQuery)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        schoolID = rs.getString("schoolID");
                    }
                }
            }

            if (schoolID == null) {
                throw new IllegalArgumentException("No schoolID found for the given id and userID");
            }

            // Step 2: Get crew details with status 'Pending'
            List<Map<String, String>> pendingCrewList = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(getPendingCrewQuery)) {
                stmt.setString(1, schoolID);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> crewMap = new HashMap<>();
                        crewMap.put("crewID", rs.getString("crewID"));
                        crewMap.put("abilities", rs.getString("abilities"));
                        crewMap.put("status", rs.getString("status"));
                        crewMap.put("session", rs.getString("session"));
                        crewMap.put("className", rs.getString("className"));
                        crewMap.put("schoolID", rs.getString("schoolID"));
                        crewMap.put("userID", rs.getString("userID"));
                        pendingCrewList.add(crewMap);
                    }
                }
            }

            // Step 3: Get user details for each userID from the crew list
            for (Map<String, String> crew : pendingCrewList) {
                try (PreparedStatement stmt = conn.prepareStatement(getUserDetailsQuery)) {
                    stmt.setString(1, crew.get("userID"));
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            Map<String, Object> crewDetail = new HashMap<>(crew);
                            crewDetail.put("userName", rs.getString("name"));
                            crewDetail.put("userEmail", rs.getString("email"));
                            crewDetail.put("userPhone", rs.getString("contactNo"));
                            crewDetails.add(crewDetail);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return crewDetails;
    }
    
    // New method to update student status in bulk
    public static void updateStudentStatus(List<Integer> studentIds, String status) {
        // Dynamically construct the placeholders for the IN clause
        String placeholders = studentIds.stream()
                                         .map(id -> "?")
                                         .collect(Collectors.joining(","));
        
        String updateStatusQuery = 
            "UPDATE tvpss_crew_info SET status = ? WHERE crewID IN (" + placeholders + ")";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(updateStatusQuery)) {
            
            // Set the status parameter
            stmt.setString(1, status);

            // Set each student ID parameter
            for (int i = 0; i < studentIds.size(); i++) {
                stmt.setInt(i + 2, studentIds.get(i)); // Start from index 2 because index 1 is for status
            }

            // Execute the update query
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public static Map<String, Object> getSchoolsAndUsers() {
        Map<String, Object> result = new HashMap<>();
        List<School> schools = new ArrayList<>();
        List<UserModel> users = new ArrayList<>();

        String query = "SELECT s.schoolID, " +
                "s.name AS schoolName, " +
                "s.state, " +
                "s.fullAddress, " +
                "s.contactNo AS schoolContactNo, " +
                "s.versionImageURL, " +
                "s.districtID AS schoolDistrictID, " +
                "s.tvpssVersion, " +
                "u.id AS userID, " +
                "u.userID AS userUserID, " +
                "u.name AS userName, " +
                "u.contactNo AS userContactNo, " +
                "u.email, " +
                "u.status, " +
                "u.role, " +
                "u.password, " +
                "u.lastActive, " +
                "u.session, " +
                "u.districtID AS userDistrictID, " +
                "u.schoolID AS userSchoolID " +
                "FROM school s " +
                "INNER JOIN user u ON u.schoolID = s.schoolID " +
                "WHERE u.role = 'Teacher' AND u.schoolID = s.schoolID";



        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        	     PreparedStatement stmt = conn.prepareStatement(query);
        	     ResultSet rs = stmt.executeQuery()) {

        	    while (rs.next()) {
        	        // Populate School object
        	        School school = new School();
        	        school.setSchoolID(rs.getInt("schoolID"));
        	        school.setName(rs.getString("schoolName"));
        	        school.setState(rs.getString("state"));
        	        school.setFullAddress(rs.getString("fullAddress"));
        	        school.setContactNo(rs.getString("schoolContactNo"));
        	        school.setVersionImageURL(rs.getString("versionImageURL"));
//        	        school.setLogo(rs.getBytes("logo"));
        	        school.setDistrictID(rs.getInt("schoolDistrictID"));
        	        school.setTvpssVersion(rs.getInt("tvpssVersion"));

        	        // Add school to list
        	        schools.add(school);

        	        // Populate UserModel object
        	        if (rs.getInt("userID") != 0) { // Ensure there's user data
        	            UserModel user = new UserModel();
        	            user.setId(rs.getInt("userID"));
        	            user.setUserID(rs.getString("userUserID"));
        	            user.setName(rs.getString("userName"));
        	            user.setContactNo(rs.getString("userContactNo"));
        	            user.setEmail(rs.getString("email"));
        	            user.setStatus(rs.getString("status"));
        	            user.setRole(rs.getString("role"));
        	            user.setPassword(rs.getString("password"));
        	            user.setLastActive(rs.getTimestamp("lastActive"));
        	            user.setSession(rs.getString("session"));
        	            user.setDistrictID(rs.getInt("userDistrictID"));
        	            user.setSchoolID(rs.getInt("userSchoolID"));

        	            // Add user to list
        	            users.add(user);
        	        }
        	    }
        	} catch (SQLException e) {
        	    e.printStackTrace();
        	}


        // Add lists to the result map
        result.put("schools", schools);
        result.put("users", users);

        return result;
    }
    
    public static List<String> getCrewNamesByDistrictIDs(List<Integer> districtIDs) {
        List<String> crewNames = new ArrayList<>();
        
        // Build the SQL query with a dynamic number of ? placeholders
        StringBuilder query = new StringBuilder("SELECT name FROM district WHERE districtID IN (");
        for (int i = 0; i < districtIDs.size(); i++) {
            query.append("?");
            if (i < districtIDs.size() - 1) {
                query.append(", ");
            }
        }
        query.append(")");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            // Set each districtID as a separate parameter
            for (int i = 0; i < districtIDs.size(); i++) {
                stmt.setInt(i + 1, districtIDs.get(i)); // Use setLong for Long type
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    crewNames.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return crewNames;
    }

    public static Map<String, Object> getTVPSSCrewVersionInfo(int schoolId) {
        Map<String, Object> schoolData = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Query to get school information
            String schoolQuery = "SELECT * FROM school WHERE schoolID = ?";
            try (PreparedStatement schoolStmt = conn.prepareStatement(schoolQuery)) {
                schoolStmt.setInt(1, schoolId);

                try (ResultSet rs = schoolStmt.executeQuery()) {
                    if (rs.next()) {
                        schoolData.put("id", rs.getInt("schoolID"));
                        schoolData.put("name", rs.getString("name"));
                        schoolData.put("address", rs.getString("fullAddress"));
                        schoolData.put("version", rs.getString("tvpssVersion"));
                        schoolData.put("image", rs.getString("versionImageURL"));
                    }
                }
            }

            // Get associated crew members with names
            List<Map<String, Object>> crewsWithNames = getCrewsWithNamesBySchoolId((int) schoolId);
            List<String> crewNames = new ArrayList<>();

            for (Map<String, Object> crewData : crewsWithNames) {
                System.out.println("Crew name: " + crewData.get("name"));
                crewNames.add((String) crewData.get("name"));
            }
            schoolData.put("crew", crewNames);

            // Get associated teacher
            UserModel teacher = getTeacherBySchoolId((int) schoolId);
            schoolData.put("teacher", teacher != null ? teacher.getName() : "N/A");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schoolData;
    }

    
    public static Integer getDistrictIdByUserId(Integer userID) {
        if (userID == null) {
            throw new IllegalStateException("User ID cannot be null.");
        }

        Integer districtID = null;

        // Database connection
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // SQL query to fetch the districtID
            String query = "SELECT districtID FROM user WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userID);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        districtID = rs.getInt("districtID");
                    } else {
                        throw new IllegalStateException("No district found for the provided user ID.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch district ID from the database.");
        }

        return districtID;
    }
    
    public static Map<String, Object> getSchoolsAndUsersByDistrict(Integer districtID) {
        Map<String, Object> result = new HashMap<>();
        List<School> schools = new ArrayList<>();
        List<UserModel> users = new ArrayList<>();

        String query = "SELECT s.schoolID, " +
                "s.name AS schoolName, " +
                "s.state, " +
                "s.fullAddress, " +
                "s.contactNo AS schoolContactNo, " +
                "s.versionImageURL, " +
                "s.districtID AS schoolDistrictID, " +
                "s.tvpssVersion, " +
                "u.id AS userID, " +
                "u.userID AS userUserID, " +
                "u.name AS userName, " +
                "u.contactNo AS userContactNo, " +
                "u.email, " +
                "u.status, " +
                "u.role, " +
                "u.password, " +
                "u.lastActive, " +
                "u.session, " +
                "u.districtID AS userDistrictID, " +
                "u.schoolID AS userSchoolID " +
                "FROM school s " +
                "INNER JOIN user u ON u.schoolID = s.schoolID " +
                "WHERE u.role = 'Teacher' AND s.districtID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the districtID in the query
            stmt.setInt(1, districtID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Populate School object
                    School school = new School();
                    school.setSchoolID(rs.getInt("schoolID"));
                    school.setName(rs.getString("schoolName"));
                    school.setState(rs.getString("state"));
                    school.setFullAddress(rs.getString("fullAddress"));
                    school.setContactNo(rs.getString("schoolContactNo"));
                    school.setVersionImageURL(rs.getString("versionImageURL"));
//                    school.setLogo(rs.getBytes("logo"));
                    school.setDistrictID(rs.getInt("schoolDistrictID"));
                    school.setTvpssVersion(rs.getInt("tvpssVersion"));

                    // Add school to list
                    schools.add(school);

                    // Populate UserModel object
                    if (rs.getInt("userID") != 0) { // Ensure there's user data
                        UserModel user = new UserModel();
                        user.setId(rs.getInt("userID"));
                        user.setUserID(rs.getString("userUserID"));
                        user.setName(rs.getString("userName"));
                        user.setContactNo(rs.getString("userContactNo"));
                        user.setEmail(rs.getString("email"));
                        user.setStatus(rs.getString("status"));
                        user.setRole(rs.getString("role"));
                        user.setPassword(rs.getString("password"));
                        user.setLastActive(rs.getTimestamp("lastActive"));
                        user.setSession(rs.getString("session"));
                        user.setDistrictID(rs.getInt("userDistrictID"));
                        user.setSchoolID(rs.getInt("userSchoolID"));

                        // Add user to list
                        users.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add lists to the result map
        result.put("schools", schools);
        result.put("users", users);

        return result;
    }

    public static List<Map<String, Object>> getTVPSSVersionApplication(int userId) {
        List<Map<String, Object>> applicationDetails = new ArrayList<>();

        // Queries
        String getDistrictIDQuery =
            "SELECT districtID FROM user WHERE id = ?";
        String getApplicationQuery =
            "SELECT v.id, v.schoolID, v.dateApplied, v.url, v.status, v.versionApplied " +
            "FROM tvpssversionapplication v " +
            "JOIN school s ON v.schoolID = s.schoolID " +
            "WHERE s.districtID = ? AND status = 'Pending'";
        String getSchoolDetailsQuery =
            "SELECT schoolID, name AS schoolName, fullAddress, contactNo, versionImageURL FROM school WHERE schoolID = ?";
        String getTeacherDetailsQuery =
            "SELECT id, name, email, contactNo FROM user WHERE role = 'Teacher' AND schoolID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Step 1: Get districtID for the given user ID
            String districtID = null;
            try (PreparedStatement stmt = conn.prepareStatement(getDistrictIDQuery)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        districtID = rs.getString("districtID");
                    }
                }
            }

            if (districtID == null) {
                throw new IllegalArgumentException("No districtID found for the given user ID");
            }

            // Step 2: Get application details matching the districtID
            List<Map<String, String>> applicationList = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(getApplicationQuery)) {
                stmt.setString(1, districtID);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> appMap = new HashMap<>();
                        appMap.put("id", rs.getString("id"));
                        appMap.put("schoolID", rs.getString("schoolID"));
                        appMap.put("dateApplied", rs.getString("dateApplied"));
                        appMap.put("url", rs.getString("url"));
                        appMap.put("status", rs.getString("status"));
                        appMap.put("versionApplied", rs.getString("versionApplied"));
                        applicationList.add(appMap);
                    }
                }
            }

            // Step 3: For each application, fetch school details and teacher details
            for (Map<String, String> application : applicationList) {
                Map<String, Object> applicationDetail = new HashMap<>(application);

                // Fetch school details
                try (PreparedStatement stmt = conn.prepareStatement(getSchoolDetailsQuery)) {
                    stmt.setString(1, application.get("schoolID"));
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                        	System.out.println("school "+ rs.getString("schoolName"));
                            applicationDetail.put("schoolName", rs.getString("schoolName"));
                            applicationDetail.put("schoolAddress", rs.getString("fullAddress"));
                            applicationDetail.put("schoolContact", rs.getString("contactNo"));
                        }
                    }
                }

                // Fetch teacher details associated with the application
                List<Map<String, String>> teacherDetails = new ArrayList<>();
                try (PreparedStatement stmt = conn.prepareStatement(getTeacherDetailsQuery)) {
                    stmt.setString(1, application.get("schoolID"));
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            Map<String, String> teacher = new HashMap<>();
                            System.out.println("teacher "+ rs.getString("name"));
                            teacher.put("id", rs.getString("id"));
                            teacher.put("teacherName", rs.getString("name"));
                            teacher.put("email", rs.getString("email"));
                            teacher.put("contactNo", rs.getString("contactNo"));
                            teacherDetails.add(teacher);
                        }
                    }
                }
                applicationDetail.put("teacherDetails", teacherDetails);

                // Add to the final details list
                applicationDetails.add(applicationDetail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return applicationDetails;
    }



    // Update the status of the application to 'Approved' based on the given id
 // Update the status of the application to 'Approved' and increment the TVPSS_Version for the related school
    public static boolean updateApproveApplicationTvpssVersion(int id) {
        String updateStatusQuery = 
            "UPDATE tvpssversionapplication SET status = 'Approved' WHERE id = ?";
        String getSchoolIdQuery = 
            "SELECT schoolID FROM tvpssversionapplication WHERE id = ?";
        String updateSchoolVersionQuery = 
            "UPDATE school SET tvpssVersion = tvpssVersion + 1 WHERE schoolID = ?";
        String getEmailQuery = 
            "SELECT email FROM user WHERE schoolID = ? AND role = 'Teacher'";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Set auto-commit to false for transaction management
            conn.setAutoCommit(false);

            // Update the status to 'Approved'
            try (PreparedStatement updateStatusStmt = conn.prepareStatement(updateStatusQuery)) {
                updateStatusStmt.setInt(1, id);
                int rowsAffected = updateStatusStmt.executeUpdate();
                if (rowsAffected == 0) {
                    conn.rollback();
                    return false; // No rows updated
                }
            }

            // Get the schoolID associated with the application
            int schoolID;
            try (PreparedStatement getSchoolIdStmt = conn.prepareStatement(getSchoolIdQuery)) {
                getSchoolIdStmt.setInt(1, id);
                try (ResultSet rs = getSchoolIdStmt.executeQuery()) {
                    if (rs.next()) {
                        schoolID = rs.getInt("schoolID");
                    } else {
                        conn.rollback();
                        return false; // No schoolID found
                    }
                }
            }

            // Increment the TVPSS_Version for the retrieved schoolID
            try (PreparedStatement updateSchoolVersionStmt = conn.prepareStatement(updateSchoolVersionQuery)) {
                updateSchoolVersionStmt.setInt(1, schoolID);
                int rowsAffected = updateSchoolVersionStmt.executeUpdate();
                if (rowsAffected == 0) {
                    conn.rollback();
                    return false; // No rows updated in the school table
                }
            }

            // Fetch the email of the teacher
            String teacherEmail = null;
            try (PreparedStatement getEmailStmt = conn.prepareStatement(getEmailQuery)) {
                getEmailStmt.setInt(1, schoolID);
                try (ResultSet rs = getEmailStmt.executeQuery()) {
                    if (rs.next()) {
                        teacherEmail = rs.getString("email");
                    }
                }
            }

            // Commit the transaction
            conn.commit();

            // If an email is found, send the approval email
            if (teacherEmail != null) {
                EmailService.sendApprovalEmail(teacherEmail);
            }
            return true;
        } catch (SQLException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false; // Return false if an exception occurs
        }
    }

    
    public static boolean updateRejectedApplicationTvpssVersion(int id, String rejectReason) throws UnsupportedEncodingException {
    	  String updateStatusQuery = 
    		        "UPDATE tvpssversionapplication SET status = 'Rejected', rejectReason = ? WHERE id = ?";
    		    String getSchoolIDQuery = 
    		        "SELECT schoolID FROM tvpssversionapplication WHERE id = ?";
    		    String getTeacherEmailQuery = 
    		        "SELECT email FROM user WHERE schoolID = ? AND role = 'Teacher'";

    		    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
    		        conn.setAutoCommit(false); // Start transaction

    		        // Step 1: Update the application status and reject reason
    		        try (PreparedStatement updateStmt = conn.prepareStatement(updateStatusQuery)) {
    		            updateStmt.setString(1, rejectReason);
    		            updateStmt.setInt(2, id);
    		            int rowsAffected = updateStmt.executeUpdate();

    		            if (rowsAffected == 0) {
    		                conn.rollback();
    		                return false; // No rows updated
    		            }
    		        }

    		        // Step 2: Get the schoolID from tvpssversionapplication
    		        int schoolID;
    		        try (PreparedStatement getSchoolStmt = conn.prepareStatement(getSchoolIDQuery)) {
    		            getSchoolStmt.setInt(1, id);
    		            ResultSet rs = getSchoolStmt.executeQuery();
    		            if (rs.next()) {
    		                schoolID = rs.getInt("schoolID");
    		            } else {
    		                conn.rollback();
    		                return false; // No schoolID found
    		            }
    		        }

    		        // Step 3: Get the email of the teacher from the user table
    		        String teacherEmail = null;
    		        try (PreparedStatement getEmailStmt = conn.prepareStatement(getTeacherEmailQuery)) {
    		            getEmailStmt.setInt(1, schoolID);
    		            ResultSet rs = getEmailStmt.executeQuery();
    		            if (rs.next()) {
    		                teacherEmail = rs.getString("email");
    		            } else {
    		                conn.rollback();
    		                return false; // No teacher email found
    		            }
    		        }

    		        // Step 4: Commit the transaction
    		        conn.commit();

    		        // Step 5: Send the rejection email
    		        if (teacherEmail != null) {
    		            EmailService.sendRejectionEmail(teacherEmail, rejectReason);
    		        }
    		        return true;

    		    } catch (SQLException e) {
    		        e.printStackTrace();
    		        return false; // Return false if an exception occurs
    		    }
    }






}