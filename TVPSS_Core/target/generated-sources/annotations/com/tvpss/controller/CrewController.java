package com.tvpss.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tvpss.model.CrewModel;

@Controller
public class CrewController {
	@GetMapping("/studentMainView")
	public String home(Model model) {
	    System.out.println("I'm in");

	    // Example student details
	    String role = "student"; // This could come from a user service or session
	    
	    // Add role to the model
	    model.addAttribute("role", role);
	    String studentName = "John Doe";
	    String studentEmail = "johndoe@example.com";

	    // Example sessions data (session name, session email, status)
	    List<CrewModel.Session> sessions = new ArrayList<>();
	    sessions.add(new CrewModel.Session("Session 1", "pic1@example.com", "Approved"));
	    sessions.add(new CrewModel.Session("Session 2", "pic2@example.com", "Rejected"));

	    // Create CrewModel object
	    CrewModel crewModel = new CrewModel(studentName, studentEmail, sessions);

	    // Add the CrewModel object to the model
	    model.addAttribute("crewModel", crewModel);

	    // Return the view name
	    return "crewVersionModule/studentMainView";
	}
	
	@GetMapping("/teacherMainView")
	public String teacherHome(Model model) {
	    System.out.println("I'm in");

	    String role = "teacher";
	    model.addAttribute("role", role);

	    // Add dynamic data
	    List<Map<String, Object>> schools = new ArrayList<>();
	    Map<String, Object> school1 = new HashMap<>();
	    school1.put("name", "Example School 1");
	    school1.put("crew", Arrays.asList("John Doe", "Jane Smith", "Michael Brown", "Emily White", "William Lee"));
	    school1.put("teacher", "Mr. John");
	    school1.put("address", "123 School Rd, City");
	    school1.put("version", "Version 1.2");

	    Map<String, Object> school2 = new HashMap<>();
	    school2.put("name", "Example School 2");
	    school2.put("crew", Arrays.asList("Alice Johnson", "Bob Williams", "Charlie Davis"));
	    school2.put("teacher", "Ms. Alice");
	    school2.put("address", "456 School Ave, City");
	    school2.put("version", "Version 2.0");

	    schools.add(school1);

	    model.addAttribute("schools", schools);

	    // Return the view name
	    return "crewVersionModule/teacherMainView";
	}

}
