package com.tvpss.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tvpss.model.CrewModel;
import com.tvpss.model.School;
import com.tvpss.model.SchoolModel;
import com.tvpss.model.UserModel;
import com.tvpss.service.CrewService;

@Controller
public class CrewController {
	@RequestMapping("/studentMainView")
	public String home(Model model, HttpSession httpSession) {
	    System.out.println("I'm in");

	    // Get the userID from the session
//	    String userID = (String) httpSession.getAttribute("userID");
	    String userID = "1";
	    if (userID == null) {
	        return "redirect:/login"; // Redirect to login if no userID in session
	    }

	    // Fetch user details from the service
	    UserModel user = CrewService.getUserDetails(userID);

	    // Fetch application details (crew information) from the service
	    List<CrewModel> crewDetails = CrewService.getCrewDetailsWithTeachers(userID);

	    if (user == null) {
	        return "redirect:/login"; // Redirect if user details are not found
	    }

	    // Add user role and details to the model
	    model.addAttribute("role", user.getRole());
	    model.addAttribute("user", user);

	    // Add application (crew) details to the model
	    model.addAttribute("crewDetails", crewDetails);

	    // Return the view name
	    return "crewVersionModule/studentMainView";
	}

    @PostMapping("/submitApplication")
    public String submitApplication(
            @RequestParam("studentName") String studentName,
            @RequestParam("email") String email,
            @RequestParam("contactNo") String contactNo,
            @RequestParam("studentClass") String studentClass,
            @RequestParam("abilities") String abilities,
            @RequestParam("id") String id,
            @RequestParam("schoolID") String schoolID,
            Model model) {

        // Calculate session based on the current year
        int currentYear = java.time.Year.now().getValue();
        String session = currentYear + "/" + (currentYear + 1);

        // Set the status to "Pending"
        String status = "Pending";
        System.out.println("email "+ email);
        // Call the service layer to insert the application details
        boolean isInserted = CrewService.insertApplication(studentName, email, contactNo, studentClass, abilities, session, status, id, schoolID);

        if (isInserted) {
            model.addAttribute("message", "Application submitted successfully!");
        } else {
            model.addAttribute("message", "Failed to submit application. Please try again.");
        }

        return "forward:/studentMainView";
    }

    // END OF STUDENT CONTROLLER METHODS
    
    //START OF TEACHER CONTROLLER METHODS
    
    @GetMapping("/teacherMainView")
    public String teacherHome(Model model) {
        System.out.println("I'm in");

        String role = "teacher";
        model.addAttribute("role", role);

        // Fetch dynamic school data from CrewService
//	    String userID = (String) httpSession.getAttribute("userID");
	    String userID = "1";
        School schoolInfo = CrewService.getSchoolDetailsByUserID(userID);
        List<Map<String, Object>> schools = new ArrayList<>();
        
            Map<String, Object> schoolData = new HashMap<>();
            schoolData.put("name", schoolInfo.getName());
            schoolData.put("address", schoolInfo.getFullAddress());
            schoolData.put("version", schoolInfo.getTvpssVersion());

            // Get associated crew members with names
            List<Map<String, Object>> crewsWithNames = CrewService.getCrewsWithNamesBySchoolId(schoolInfo.getSchoolID());
            List<String> crewNames = new ArrayList<>();

            for (Map<String, Object> crewData : crewsWithNames) {
            	System.out.println("name is crewData "+ crewData.get("name"));
                crewNames.add((String) crewData.get("name"));
            }
            schoolData.put("crew", crewNames);

            // Get associated teacher
            UserModel teacher = CrewService.getTeacherBySchoolId(schoolInfo.getSchoolID());
            schoolData.put("teacher", teacher != null ? teacher.getName() : "N/A");

            schools.add(schoolData);
            System.out.println("school Data "+ schools);
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
