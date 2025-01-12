package com.tvpss.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tvpss.model.ContentModel;
import com.tvpss.model.School;

public class DashboardService {
	
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
    
    public static int getUserCount() {
        int userCount = 0;

        // Define the query to count rows in the user table
        String query = "SELECT COUNT(*) AS total FROM user";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Fetch the count from the result set
            if (rs.next()) {
                userCount = rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle exceptions as necessary
        }

        return userCount;
    }
    
    public static int getActiveUserCount() {
        int activeUserCount = 0;

        // Get the current date and format it to be compatible with the database format
        LocalDate currentDate = LocalDate.now();
        LocalDate threeDaysAgo = currentDate.minusDays(3);

        String query = "SELECT COUNT(*) AS activeCount FROM user WHERE lastActive >= ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameter for the date that is 3 days ago
            stmt.setString(1, threeDaysAgo.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    activeUserCount = rs.getInt("activeCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return activeUserCount;
    }
    
    public static int getInactiveUserCount() {
        int inactiveUserCount = 0;

        // Get the current date and format it to be compatible with the database format
        LocalDate currentDate = LocalDate.now();
        LocalDate thirtyDaysAgo = currentDate.minusDays(30);

        String query = "SELECT COUNT(*) AS activeCount FROM user WHERE lastActive <= ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameter for the date that is 3 days ago
            stmt.setString(1, thirtyDaysAgo.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    inactiveUserCount = rs.getInt("inactiveCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return inactiveUserCount;
    }
    
    public static int getUsersActiveToday() {
        int activeTodayCount = 0;

        String query = "SELECT COUNT(*) AS activeTodayCount FROM user WHERE DATE(lastActive) = CURDATE()";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    activeTodayCount = rs.getInt("activeTodayCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return activeTodayCount;
    }
    
    
    public static int getStudentActiveToday() {
        int studentTodayCount = 0;

        String query = "SELECT COUNT(*) AS studentTodayCount FROM user WHERE DATE(lastActive) = CURDATE() AND role = 'student'";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                	studentTodayCount = rs.getInt("studentTodayCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return studentTodayCount;
    }
    
    public static int getTeacherActiveToday() {
        int teacherTodayCount = 0;

        String query = "SELECT COUNT(*) AS teacherTodayCount FROM user WHERE DATE(lastActive) = CURDATE() AND role = 'teacher'";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                	teacherTodayCount = rs.getInt("teacherTodayCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return teacherTodayCount;
    }
    
    public static int getDistrictActiveToday() {
        int districtTodayCount = 0;

        String query = "SELECT COUNT(*) AS teacherTodayCount FROM user WHERE DATE(lastActive) = CURDATE() AND role = 'district'";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                	districtTodayCount = rs.getInt("districtTodayCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return districtTodayCount;
    }
    
    public static int getTotalSchoolsByDistrict(int districtID) {
        int totalSchools = 0;

        // SQL query to count schools under the given districtID
        String query = "SELECT COUNT(*) AS schoolCount FROM school WHERE districtID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the districtID parameter
            stmt.setInt(1, districtID);

            // Execute the query and process the result
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalSchools = rs.getInt("schoolCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return totalSchools;
    }
    
 // Function to get the count of schools with studioLevelStatus as "approved"
    public static int getApprovedSchoolsByDistrict(int districtID) {
        return getSchoolsByStudioLevelStatus(districtID, "approved");
    }

    // Function to get the count of schools with studioLevelStatus as "pending"
    public static int getPendingSchoolsByDistrict(int districtID) {
        return getSchoolsByStudioLevelStatus(districtID, "pending");
    }

    // Function to get the count of schools with studioLevelStatus as "rejected"
    public static int getRejectedSchoolsByDistrict(int districtID) {
        return getSchoolsByStudioLevelStatus(districtID, "rejected");
    }

    // Common utility function for fetching schools based on studioLevelStatus
    private static int getSchoolsByStudioLevelStatus(int districtID, String status) {
        int count = 0;

        String query = "SELECT COUNT(*) AS schoolCount " +
                       "FROM school s " +
                       "JOIN studio st ON s.studioID = st.studioID " +
                       "WHERE s.districtID = ? AND st.studioLevelStatus = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters for districtID and studioLevelStatus
            stmt.setInt(1, districtID);
            stmt.setString(2, status);

            // Execute the query and process the result
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("schoolCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
 // Function to get the count of schools with status as "Approved"
    public static int getApprovedTVPSSApplicationsByDistrict(int districtID) {
        return getTVPSSApplicationsByStatus(districtID, "Approved");
    }

    // Function to get the count of schools with status as "Pending"
    public static int getPendingTVPSSApplicationsByDistrict(int districtID) {
        return getTVPSSApplicationsByStatus(districtID, "Pending");
    }

    // Function to get the count of schools with status as "Rejected"
    public static int getRejectedTVPSSApplicationsByDistrict(int districtID) {
        return getTVPSSApplicationsByStatus(districtID, "Rejected");
    }

    // Common utility function for fetching TVPSS applications based on status
    private static int getTVPSSApplicationsByStatus(int districtID, String status) {
        int count = 0;

        String query = "SELECT COUNT(*) AS applicationCount " +
                       "FROM school s " +
                       "JOIN tvpssversionapplication tva ON s.schoolID = tva.schoolID " +
                       "WHERE s.districtID = ? AND tva.status = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters for districtID and status
            stmt.setInt(1, districtID);
            stmt.setString(2, status);

            // Execute the query and process the result
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("applicationCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getL1StudioByDistrict(int districtID) {
        int count = 0;

        String query = "SELECT COUNT(*) AS studioCount " +
                       "FROM school s " +
                       "JOIN studio st ON s.studioID = st.studioID " +
                       "WHERE s.districtID = ? AND st.studioLevel = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameter for districtID
            stmt.setInt(1, districtID);

            // Execute the query and process the result
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("studioCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getL2StudioByDistrict(int districtID) {
        int count = 0;

        String query = "SELECT COUNT(*) AS studioCount " +
                       "FROM school s " +
                       "JOIN studio st ON s.studioID = st.studioID " +
                       "WHERE s.districtID = ? AND st.studioLevel = 2";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameter for districtID
            stmt.setInt(1, districtID);

            // Execute the query and process the result
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("studioCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getL3StudioByDistrict(int districtID) {
        int count = 0;

        String query = "SELECT COUNT(*) AS studioCount " +
                       "FROM school s " +
                       "JOIN studio st ON s.studioID = st.studioID " +
                       "WHERE s.districtID = ? AND st.studioLevel = 3";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameter for districtID
            stmt.setInt(1, districtID);

            // Execute the query and process the result
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("studioCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getTVPSSV1CountByDistrict(int districtID) {
        int count = 0;

        String query = "SELECT COUNT(*) AS version1Count FROM school WHERE districtID = ? AND tvpssVersion = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameter for the districtID
            stmt.setInt(1, districtID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("version1Count");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getTVPSSV2CountByDistrict(int districtID) {
        int count = 0;

        String query = "SELECT COUNT(*) AS version1Count FROM school WHERE districtID = ? AND tvpssVersion = 2";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameter for the districtID
            stmt.setInt(1, districtID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("version1Count");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }

    
    public static int getTVPSSV3CountByDistrict(int districtID) {
        int count = 0;

        String query = "SELECT COUNT(*) AS version1Count FROM school WHERE districtID = ? AND tvpssVersion = 3";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameter for the districtID
            stmt.setInt(1, districtID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("version1Count");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getTVPSSV4CountByDistrict(int districtID) {
        int count = 0;

        String query = "SELECT COUNT(*) AS version1Count FROM school WHERE districtID = ? AND tvpssVersion = 4";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameter for the districtID
            stmt.setInt(1, districtID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("version1Count");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getStateTotalSchool() {
        int count = 0;

        String query = "SELECT COUNT(*) AS totalSchool FROM school";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("totalSchool");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getStateTotalDistrict() {
        int count = 0;

        String query = "SELECT COUNT(*) AS totalDistrict FROM district";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("totalDistrict");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getContentCurMonth() {
        int contentCount = 0;
        
        // Get the current month and year
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        String currentMonthYear = sdf.format(Calendar.getInstance().getTime());

        String query = "SELECT COUNT(*) AS contentCount FROM content_info WHERE DATE_FORMAT(STR_TO_DATE(date, '%d/%m/%Y'), '%m/%Y') = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameter for current month and year
            stmt.setString(1, currentMonthYear);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    contentCount = rs.getInt("contentCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return contentCount;
    }
    
    public static int getL1Studio() {
        int count = 0;

        String query = "SELECT COUNT(*) AS studioCount " +
                       "FROM studio " +
                       "WHERE studioLevel = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Execute the query and process the result
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("studioCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getL2Studio() {
        int count = 0;

        String query = "SELECT COUNT(*) AS studioCount " +
                       "FROM studio " +
                       "WHERE studioLevel = 2";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Execute the query and process the result
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("studioCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getL3Studio() {
        int count = 0;

        String query = "SELECT COUNT(*) AS studioCount " +
                       "FROM studio " +
                       "WHERE studioLevel = 3";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Execute the query and process the result
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("studioCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getTVPSSV1Count() {
        int count = 0;

        String query = "SELECT COUNT(*) AS version1Count FROM school WHERE tvpssVersion = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("version1Count");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getTVPSSV2Count() {
        int count = 0;

        String query = "SELECT COUNT(*) AS version1Count FROM school WHERE tvpssVersion = 2";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("version2Count");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getTVPSSV3Count() {
        int count = 0;

        String query = "SELECT COUNT(*) AS version1Count FROM school WHERE tvpssVersion = 3";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("version3Count");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getTVPSSV4Count() {
        int count = 0;

        String query = "SELECT COUNT(*) AS version1Count FROM school WHERE tvpssVersion = 4";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("version4Count");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return count;
    }
    
    public static int getContentCountByMonth(String monthName) {
        int contentCount = 0;

        // Get the current year
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        
        // Convert month name to month number (1 = January, 12 = December)
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
        int month = 0;
        try {
            Date monthDate = monthFormat.parse(monthName);
            calendar.setTime(monthDate);
            month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based, so add 1
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception if needed
        }

        String query = "SELECT COUNT(*) AS contentCount FROM content_info WHERE DATE_FORMAT(STR_TO_DATE(date, '%d/%m/%Y'), '%m/%Y') = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameter for month and year (current year)
            String monthYear = String.format("%02d/%d", month, currentYear);
            stmt.setString(1, monthYear);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    contentCount = rs.getInt("contentCount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return contentCount;
    }



    
  
}
