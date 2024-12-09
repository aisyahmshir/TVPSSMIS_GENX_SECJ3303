package com.tvpss.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
	    school1.put("name", "SMK Pagoh");
	    school1.put("crew", Arrays.asList("John Doe", "Jane Smith", "Michael Brown", "Emily White", "William Lee"));
	    school1.put("teacher", "Mr. John");
	    school1.put("address", "123 School Rd, City");
	    school1.put("version", "Version 2");

	    Map<String, Object> school2 = new HashMap<>();
	    school2.put("name", "Example School 2");
	    school2.put("crew", Arrays.asList("Alice Johnson", "Bob Williams", "Charlie Davis"));
	    school2.put("teacher", "Ms. Alice");
	    school2.put("address", "456 School Ave, City");
	    school2.put("version", "Version 2.0");

	    schools.add(school1);

	    model.addAttribute("schools", schools);

	    return "crewVersionModule/teacherMainView";
	}
	
	@GetMapping("/teacherViewApplication")
	public String teacherViewApplication(Model model) {
	    System.out.println("I'm in");

	    String role = "teacher";
	    model.addAttribute("role", role);

	    // Add dynamic data
	    List<Map<String, Object>> students = new ArrayList<>();
	    Map<String, Object> student1 = new HashMap<>();
	    student1.put("name", "John Doe");
	    student1.put("class", "Grade 10 - A");
	    student1.put("address", "123 Main St, City");
	    student1.put("abilities", "John has shown excellent abilities in Math, Science, English, History, and Art. He enjoys learning new subjects and has a natural talent for problem-solving. His performance has been consistently outstanding, and he is always eager to help his classmates.");

	    Map<String, Object> student2 = new HashMap<>();
	    student2.put("name", "Jane Smith");
	    student2.put("class", "Grade 11 - B");
	    student2.put("address", "456 Oak St, City");
	    student2.put("abilities", "Jane excels in English, Science, and Math. Her passion for learning is evident in her constant curiosity and dedication to her studies. She also has a talent for music, which she actively participates in, contributing to school events and performances.");

	    students.add(student1);
	    students.add(student2);

	    model.addAttribute("students", students);

	    return "crewVersionModule/teacherViewApplications";
	}
	
	 @GetMapping("/districtMainView")
	    public String districtMainView(Model model) {
	        System.out.println("I'm in");

	        // Add dynamic data for school list
	        List<Map<String, Object>> schools = new ArrayList<>();

	        // Data for 5 schools
	        for (int i = 1; i <= 5; i++) {
	            Map<String, Object> school = new HashMap<>();
	            school.put("name", "School " + i);
	            school.put("address", "Address " + i);
	            school.put("version", "Version " + i);

	            // Crew list for each school
	            List<String> crewList = new ArrayList<>();
	            crewList.add("Teacher A");
	            crewList.add("Teacher B");
	            crewList.add("Teacher C");

	            // Add crew list to school data
	            school.put("crew", crewList);

	            // Single image URL for each school
	            String image = "https://via.placeholder.com/"+"School " + i; // Example URL for image
	            school.put("image", image); // Use single URL instead of a list

	            // Add the school to the list
	            schools.add(school);
	        }

	        // Add the list of schools to the model
	        model.addAttribute("schools", schools);

	        return "crewVersionModule/districtMainView"; // Path to the HTML template
	    }
	 
	 @GetMapping("/stateMainView")
	 public String stateMainView(Model model) {
	     System.out.println("I'm in");

	     // List to store school data for the district
	     List<Map<String, Object>> schools = new ArrayList<>();
	     List<String> districts = new ArrayList<>();

	     // Sample data for 5 schools
	     for (int i = 1; i <= 5; i++) {
	         Map<String, Object> school = new HashMap<>();
	         school.put("id", i); // Add an ID for each school
	         school.put("name", "School " + i);
	         school.put("address", "District " + i);
	         school.put("version", "Version " + i);
		     districts.add("District "+i);

	         // Sample crew list for each school
	         List<String> crewList = new ArrayList<>();
	         crewList.add("Teacher A");
	         crewList.add("Teacher B");
	         crewList.add("Teacher C");

	         // Add crew list to school data
	         school.put("crew", crewList);

	         // Sample image URL for each school
	         String image = "https://via.placeholder.com/150?text=School+" + i;
	         school.put("image", image);

	         // Add the school to the list
	         schools.add(school);
	     }

	     // Add the schools data to the model for rendering
	     model.addAttribute("schools", schools);

	     // Add the districts to the model for the dropdown filter
	     model.addAttribute("districts", districts);

	     return "crewVersionModule/stateMainView"; // Path to the HTML template
	 }
	 
	 @GetMapping("/viewCrewSchool/{id}")
	 public String viewSchool(@PathVariable("id") int schoolId, Model model) {
	     System.out.println("View school with ID: " + schoolId);

	     // Find the school by ID (this would normally come from a database)
	     Map<String, Object> selectedSchool = null;

	     for (int i = 1; i <= 5; i++) {
	         if (i == schoolId) {
	             selectedSchool = new HashMap<>();
	             selectedSchool.put("id", i);
	             selectedSchool.put("name", "School " + i);
	             selectedSchool.put("address", "Address " + i);
	             selectedSchool.put("version", "Version " + i);

	             // Crew list for the specific school
	             List<String> crewList = new ArrayList<>();
	             crewList.add("Teacher A");
	             crewList.add("Teacher B");
	             crewList.add("Teacher C");

	             // Add crew list to school data
	             selectedSchool.put("crew", crewList);

	             // Image URL for the specific school
	             String image = "https://via.placeholder.com/" + "School " + i;
	             selectedSchool.put("image", image); // Use single URL instead of a list
	             break; // Break after finding the school
	         }
	     }

	     System.out.println(selectedSchool);

	     if (selectedSchool != null) {
	         model.addAttribute("school", selectedSchool); // Add the selected school to the model
	     } else {
	         model.addAttribute("error", "School not found"); // If no school found
	     }

	     return "crewVersionModule/stateViewMore"; // Return to the school details page
	 }

}
