package com.tvpss.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
	@GetMapping("/AdminDashboard")
    public String showPage(Model model) {
		model.addAttribute("labels", List.of("Student", "Teacher", "District"));
        model.addAttribute("data", List.of(69, 12, 5));
        return "Dashboard/AdminDashboard"; // Matches "yourfile.html" in the templates folder
    }
	
	@GetMapping("/DistrictDashboard")
	public String showDistrictDashboard(Model model) {
	    // Fixed data for the pie chart
	    model.addAttribute("pieLabels", List.of("Level 1", "Level 2", "Level 3"));
	    model.addAttribute("pieData", List.of(55, 30, 15));

	    // Fixed data for the bar chart
	    model.addAttribute("barLabels", List.of("Microphone", "Camera", "Studio Light", "Other Accesories"));
	    model.addAttribute("barData", List.of(12, 19, 3, 5));

	    return "Dashboard/DistrictDashboard"; 
	}
	
	@GetMapping("/StateDashboard")
	public String showStateDashboard(Model model) {
	    // Fixed data for the pie chart
	    model.addAttribute("pieLabels", List.of("Level 1", "Level 2", "Level 3"));
	    model.addAttribute("pieData", List.of(55, 30, 15));

	    // Fixed data for the bar chart
	    model.addAttribute("barLabels", List.of("District 1", "District 2", "District 3", "District 4"));
	    model.addAttribute("barData", List.of(12, 19, 3, 5));

	    // Fixed data for the line chart
	    model.addAttribute("lineLabels", List.of("January", "February", "March", "April", "May", "June"));
	    model.addAttribute("lineData", List.of(65, 59, 80, 81, 56, 55));

	    return "Dashboard/StateDashboard"; // Matches "StateDashboard.html" in the templates folder
	}

 
}
