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
        // If you're using Thymeleaf, return the name of the HTML file without extension
        return "Dashboard/AdminDashboard"; // Matches "yourfile.html" in the templates folder
    }
 
}
