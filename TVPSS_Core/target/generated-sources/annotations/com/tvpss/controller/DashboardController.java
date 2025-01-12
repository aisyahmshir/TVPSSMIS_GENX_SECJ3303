package com.tvpss.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpSession;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tvpss.service.DashboardService;

@Controller
public class DashboardController {
	@GetMapping("/AdminDashboard")
    public String showPage(Model model, HttpSession session) {
		int totalUser = DashboardService.getUserCount();
		int totalActiveUser = DashboardService.getActiveUserCount();
		int totalInactiveUser = DashboardService.getInactiveUserCount();
		int totalActiveToday = DashboardService.getUsersActiveToday();
		int studentTodayCount = DashboardService.getStudentActiveToday();
		int teacherTodayCount = DashboardService.getTeacherActiveToday();
		int districtTodayCount = DashboardService.getDistrictActiveToday();
		LocalDate currentDate = LocalDate.now();
	    String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")); // Adjust format as needed
	    model.addAttribute("currentDate", formattedDate);
		model.addAttribute("totalUser", totalUser);
		model.addAttribute("totalActiveUser", totalActiveUser);
		model.addAttribute("totalInactiveUser", totalInactiveUser);
		model.addAttribute("totalActiveToday", totalActiveToday);
		model.addAttribute("labels", List.of("Student", "Teacher", "District"));
        model.addAttribute("data", List.of(studentTodayCount, teacherTodayCount, districtTodayCount));
        
        String role = (String) session.getAttribute("role");
        int userID = (Integer) session.getAttribute("id");

        // Add attributes to the model
        model.addAttribute("role", role);
        model.addAttribute("userID", userID);
        
        return "Dashboard/AdminDashboard"; // Matches "yourfile.html" in the templates folder
    }
	
	@GetMapping("/DistrictDashboard")
	public String showDistrictDashboard(Model model, HttpSession session) {
		//String districtID = (String) httpSession.getAttribute("districtID");
	    int districtID = 1;
	    // Fixed data for the pie chart
	    int totalSchool = DashboardService.getTotalSchoolsByDistrict(districtID);
	    int approvedStudio = DashboardService.getApprovedSchoolsByDistrict(districtID);
	    int pendingStudio = DashboardService.getPendingSchoolsByDistrict(districtID);
	    int rejectedStudio = DashboardService.getRejectedSchoolsByDistrict(districtID);
	    int approvedVersion = DashboardService.getApprovedTVPSSApplicationsByDistrict(districtID);
	    int pendingVersion = DashboardService.getPendingTVPSSApplicationsByDistrict(districtID);
	    int rejectedVersion = DashboardService.getRejectedTVPSSApplicationsByDistrict(districtID);
	    int L1Studio = DashboardService.getL1StudioByDistrict(districtID);
	    int L1percentage = (L1Studio == 0) ? 0 : (L1Studio * 100) / totalSchool;
	    int L2Studio = DashboardService.getL2StudioByDistrict(districtID);
	    int L2percentage = (L2Studio == 0) ? 0 : (L2Studio * 100) / totalSchool;
	    int L3Studio = DashboardService.getL3StudioByDistrict(districtID);
	    int L3percentage = (L3Studio == 0) ? 0 : (L3Studio * 100) / totalSchool;
	    int TVPSSv1 = DashboardService.getTVPSSV1CountByDistrict(districtID);
	    int TVPSSv2 = DashboardService.getTVPSSV2CountByDistrict(districtID);
	    int TVPSSv3 = DashboardService.getTVPSSV3CountByDistrict(districtID);
	    int TVPSSv4 = DashboardService.getTVPSSV4CountByDistrict(districtID);
	    model.addAttribute("totalSchool", totalSchool);
	    model.addAttribute("approvedReq", approvedStudio + approvedVersion);
	    model.addAttribute("pendingReq", pendingStudio + pendingVersion);
	    model.addAttribute("rejectedReq", rejectedStudio + rejectedVersion);
	    
	    model.addAttribute("pieLabels", List.of("Level 1", "Level 2", "Level 3"));
	    model.addAttribute("pieData", List.of(L1percentage, L2percentage, L3percentage));

	    // Fixed data for the bar chart
	    model.addAttribute("barLabels", List.of("Version 1", "Version 2", "Version 3", "Version 4"));
	    model.addAttribute("barData", List.of(TVPSSv1, TVPSSv2, TVPSSv3, TVPSSv4));
	    
	    String role = (String) session.getAttribute("role");
        String userID = (String) session.getAttribute("userID");

        // Add attributes to the model
        model.addAttribute("role", role);
        model.addAttribute("userID", userID);

	    return "Dashboard/DistrictDashboard"; 
	}
	
	@GetMapping("/StateDashboard")
	public String showStateDashboard(Model model, HttpSession session) {
		
		int stateTotalSchool = DashboardService.getStateTotalSchool();
		int stateTotalDistrict = DashboardService.getStateTotalDistrict();
		int totalContentCurMonth = DashboardService.getContentCurMonth();
		
		int L1Studio = DashboardService.getL1Studio();
	    int L1percentage = (L1Studio == 0) ? 0 : (L1Studio * 100) / stateTotalSchool;
	    int L2Studio = DashboardService.getL2Studio();
	    int L2percentage = (L2Studio == 0) ? 0 : (L2Studio * 100) / stateTotalSchool;
	    int L3Studio = DashboardService.getL3Studio();
	    int L3percentage = (L3Studio == 0) ? 0 : (L3Studio * 100) / stateTotalSchool;
	    
	    int TVPSSv1 = DashboardService.getTVPSSV1Count();
	    int TVPSSv2 = DashboardService.getTVPSSV2Count();
	    int TVPSSv3 = DashboardService.getTVPSSV3Count();
	    int TVPSSv4 = DashboardService.getTVPSSV4Count();
		
		model.addAttribute("totalContentCurMonth", totalContentCurMonth);
		model.addAttribute("stateTotalDistrict", stateTotalDistrict);
		model.addAttribute("stateTotalSchool", stateTotalSchool);
	    // Fixed data for the pie chart
		model.addAttribute("pieLabels", List.of("Level 1", "Level 2", "Level 3"));
	    model.addAttribute("pieData", List.of(L1percentage, L2percentage, L3percentage));

	    // Fixed data for the bar chart
	    model.addAttribute("barLabels", List.of("Version 1", "Version 2", "Version 3", "Version 4"));
	    model.addAttribute("barData", List.of(TVPSSv1, TVPSSv2, TVPSSv3, TVPSSv4));

	    // Fixed data for the line chart
	    Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("MMMM"); // Full month name

	 // Prepare list for the months (current and previous 5 months)
	    List<String> lineLabels = new ArrayList<>();
	    
	    // Set calendar to current date, then go back 5 months to start adding
	    calendar.add(Calendar.MONTH, -5); // Go back 5 months from the current month
	    
	    // Add the 6 months, starting from 5 months ago to the current month
	    for (int i = 0; i < 6; i++) {
	        lineLabels.add(sdf.format(calendar.getTime())); // Add the month to the list
	        calendar.add(Calendar.MONTH, 1); // Move to the next month
	    }
	    
	    // Add lineLabels to the model
	    model.addAttribute("lineLabels", lineLabels);

	    // Fetch content counts for the last 6 months (current month and previous 5 months)
	    List<Integer> lineData = new ArrayList<>();
	    for (String month : lineLabels) {
	        lineData.add(DashboardService.getContentCountByMonth(month)); // Call the service for each month
	    }

	    // Add lineData to the model
	    model.addAttribute("lineData", lineData);
//	    model.addAttribute("lineLabels", List.of("January", "February", "March", "April", "May", "June"));
//	    model.addAttribute("lineData", List.of(65, 59, 80, 81, 56, 55));
	    
	    String role = (String) session.getAttribute("role");
        String userID = (String) session.getAttribute("userID");

        // Add attributes to the model
        model.addAttribute("role", role);
        model.addAttribute("userID", userID);

	    return "Dashboard/StateDashboard"; // Matches "StateDashboard.html" in the templates folder
	}

 
}
