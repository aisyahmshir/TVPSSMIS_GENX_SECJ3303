package com.tvpss.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tvpss.model.ContentModel;
import com.tvpss.model.School;

public class ContentService {

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

    /**
     * Adds content to the `content_info` table.
     */
    public static boolean addContent(ContentModel newContent, int id) {
        // Step 1: Retrieve the schoolID for the given userID
        String getSchoolIdQuery = "SELECT schoolID FROM user WHERE id = ?";
        String insertContentQuery = 
            "INSERT INTO content_info (schoolID, title, date, videoURL, eventName, details) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
        
        int schoolID = -1;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Retrieve the schoolID
            try (PreparedStatement getSchoolStmt = conn.prepareStatement(getSchoolIdQuery)) {
                getSchoolStmt.setInt(1, id);
                try (ResultSet rs = getSchoolStmt.executeQuery()) {
                    if (rs.next()) {
                        schoolID = rs.getInt("schoolID");
                    }
                }
            }

            if (schoolID == -1) {
                System.out.println("No school found for the given userID.");
                return false;
            }

            // Insert the new content
            try (PreparedStatement insertStmt = conn.prepareStatement(insertContentQuery)) {
                insertStmt.setInt(1, schoolID);
                insertStmt.setString(2, newContent.getTitle());
                insertStmt.setString(3, "date");
                insertStmt.setString(4, newContent.getVideoURL());
                insertStmt.setString(5, newContent.getEventName());
                insertStmt.setString(6, newContent.getDetails());

                int rowsInserted = insertStmt.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public static void save(ContentModel content) {
        String updateContentQuery = "UPDATE content_info SET title = ?, date = ?, eventName = ?, details = ?, videoURL = ? WHERE contentID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Prepare the SQL update statement
            try (PreparedStatement updateContentStmt = conn.prepareStatement(updateContentQuery)) {
                updateContentStmt.setString(1, content.getTitle());
                updateContentStmt.setString(2, content.getDate());
                updateContentStmt.setString(3, content.getEventName());
                updateContentStmt.setString(4, content.getDetails());
                updateContentStmt.setString(5, content.getVideoURL());
                updateContentStmt.setInt(6, content.getContentID());

                // Execute the update
                int rowsUpdated = updateContentStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Content updated successfully.");
                } else {
                    System.out.println("No content found with the given ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception as needed
        }
    }


    /**
     * Retrieves all content from the `content_info` table.
     */
    public static List<ContentModel> getAllContents() {
        List<ContentModel> contentList = new ArrayList<>();
        String query = "SELECT * FROM content_info";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ContentModel content = new ContentModel();
                content.setContentID(rs.getInt("contentID"));
                content.setSchoolID(rs.getInt("schoolID"));
                content.setTitle(rs.getString("title"));
                content.setDate(rs.getString("date"));
                content.setVideoURL(rs.getString("videoURL"));
                content.setEventName(rs.getString("eventName"));
                content.setDetails(rs.getString("details"));

                contentList.add(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contentList;
    }
    
    public static ContentModel findById(int contentID) {
        ContentModel content = null; // Initialize the content object
        String getContentByIdQuery = "SELECT * FROM content_info WHERE contentID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Retrieve the content for the given contentID
            try (PreparedStatement getContentStmt = conn.prepareStatement(getContentByIdQuery)) {
                getContentStmt.setInt(1, contentID);

                try (ResultSet rs = getContentStmt.executeQuery()) {
                    if (rs.next()) {
                        content = new ContentModel();
                        content.setContentID(rs.getInt("contentID"));
                        content.setSchoolID(rs.getInt("schoolID"));
                        content.setTitle(rs.getString("title"));
                        content.setDate(rs.getString("date"));
                        content.setVideoURL(rs.getString("videoURL"));
                        content.setEventName(rs.getString("eventName"));
                        content.setDetails(rs.getString("details"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception as needed
        }

        return content;
    }

    
    public static List<ContentModel> getAllContentBySchool(int id) {
        List<ContentModel> contentList = new ArrayList<>();
        String getSchoolIdQuery = "SELECT schoolID FROM user WHERE id = ?";
        String getContentBySchoolQuery = "SELECT * FROM content_info WHERE schoolID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Step 1: Retrieve the schoolID for the given userID
            int schoolId = -1;
            try (PreparedStatement getSchoolStmt = conn.prepareStatement(getSchoolIdQuery)) {
                getSchoolStmt.setInt(1, id);
                try (ResultSet rs = getSchoolStmt.executeQuery()) {
                    if (rs.next()) {
                        schoolId = rs.getInt("schoolID");
                    }
                }
            }

            if (schoolId == -1) {
                System.out.println("No school found for the given userID.");
                return contentList; // Return an empty list if no schoolID is found
            }

            // Step 2: Retrieve all contents for the given schoolID
            try (PreparedStatement getContentStmt = conn.prepareStatement(getContentBySchoolQuery)) {
                getContentStmt.setInt(1, schoolId);
                try (ResultSet rs = getContentStmt.executeQuery()) {
                    while (rs.next()) {
                        ContentModel content = new ContentModel();
                        content.setContentID(rs.getInt("contentID"));
                        content.setSchoolID(rs.getInt("schoolID"));
                        content.setTitle(rs.getString("title"));
                        content.setDate(rs.getString("date"));
                        content.setVideoURL(rs.getString("videoURL"));
                        content.setEventName(rs.getString("eventName"));
                        content.setDetails(rs.getString("details"));
                        contentList.add(content);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contentList;
    }
    
    public static List<ContentModel> getAllContentBySchoolID(int schoolID) {
        List<ContentModel> contentList = new ArrayList<>();
        String getContentBySchoolQuery = "SELECT * FROM content_info WHERE schoolID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Retrieve all contents for the given schoolID
            try (PreparedStatement getContentStmt = conn.prepareStatement(getContentBySchoolQuery)) {
                getContentStmt.setInt(1, schoolID);

                try (ResultSet rs = getContentStmt.executeQuery()) {
                    while (rs.next()) {
                        ContentModel content = new ContentModel();
                        content.setContentID(rs.getInt("contentID"));
                        content.setSchoolID(rs.getInt("schoolID"));
                        content.setTitle(rs.getString("title"));
                        content.setDate(rs.getString("date"));
                        content.setVideoURL(rs.getString("videoURL"));
                        content.setEventName(rs.getString("eventName"));
                        content.setDetails(rs.getString("details"));
                        contentList.add(content);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception as needed
        }

        return contentList;
    }

    
    public static List<School> getAllSchoolByDistrict(int districtID) {
        List<School> schoolList = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT schoolID, name, tvpssVersion, districtID FROM school WHERE districtID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, districtID);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        School school = new School();
                        school.setSchoolID(rs.getInt("schoolID"));
                        school.setName(rs.getString("name"));
                        school.setTvpssVersion(rs.getInt("tvpssVersion"));
                        school.setDistrictID(rs.getInt("districtID"));
                        schoolList.add(school);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception as needed
        }

        return schoolList;
    }
    
    public static String getDistrictNameByID(int districtID) {
        String districtName = null;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT name FROM district WHERE districtID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, districtID);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        districtName = rs.getString("name");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception as needed
        }

        return districtName;
    }
    
    public static List<Map<String, Object>> getAllDistricts() {
        List<Map<String, Object>> districts = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT districtID, name FROM district";

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Map<String, Object> district = new HashMap<>();
                    district.put("districtID", rs.getInt("districtID"));
                    district.put("name", rs.getString("name"));
                    districts.add(district);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception as needed
        }

        return districts;
    }

    
    



}
