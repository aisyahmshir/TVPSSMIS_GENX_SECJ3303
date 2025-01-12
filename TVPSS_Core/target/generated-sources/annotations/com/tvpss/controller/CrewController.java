package com.tvpss.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tvpss.model.CrewModel;
import com.tvpss.model.School;
import com.tvpss.model.SchoolModel;
import com.tvpss.model.UserModel;
import com.tvpss.service.CrewService;
import com.tvpss.service.EmailService;

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
            System.out.println("School ID "+ schoolInfo.getSchoolID());
            schoolData.put("schoolID", schoolInfo.getSchoolID());
            schoolData.put("name", schoolInfo.getName());
            schoolData.put("address", schoolInfo.getFullAddress());
            schoolData.put("version", schoolInfo.getTvpssVersion());
            schoolData.put("url", schoolInfo.getVersionImageURL());
            Boolean checkSubmission = CrewService.checkPendingApplication(schoolInfo.getSchoolID());

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
            schoolData.put("checkStatus", checkSubmission);
            schools.add(schoolData);
            System.out.println("school Data "+ checkSubmission);
            model.addAttribute("schools", schools);

        return "crewVersionModule/teacherMainView";
    }
    
    
    
    @PostMapping("/applyVersionUpgrade")
    public String applyVersionUpgrade(@RequestParam("schoolID") int schoolID,
                                                   @RequestParam("driveLink") String driveLink,
                                                   @RequestParam("tvpssVersion") int version,
                                                   RedirectAttributes redirectAttributes) {
    	
        java.sql.Date todayDate = new java.sql.Date(System.currentTimeMillis());
        int id = 1;
        String status = "Pending";
        System.out.println("id "+schoolID);
        System.out.println("link "+driveLink);
        System.out.println("ver "+version);
        boolean isSubmitted = CrewService.submitTVPSSApplication(schoolID,todayDate, driveLink,status, version);

        // Set a redirect attribute to indicate success or failure
        if (isSubmitted) {
            redirectAttributes.addAttribute("status", "success");
        } else {
            redirectAttributes.addAttribute("status", "error");
        }

        // Redirect to the same page with the status parameter
        return "redirect:/teacherMainView";
    }



	
    @GetMapping("/teacherViewApplication")
    public String teacherViewApplication(HttpSession session, Model model) {
        System.out.println("I'm in");

        // Get the userID from the session
        //Integer userID = (Integer) session.getAttribute("userID");
        Integer userID = 1;
        if (userID == null) {
            throw new IllegalStateException("User is not logged in.");
        }

        // Fetch applications using the service
        List<Map<String, Object>> applications = CrewService.getTVPSSApplicationCrew(userID);
        Set<String> sessionValues = applications.stream()
                .map(app -> (String) app.get("session"))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        // Add role and dynamic data to the model
        model.addAttribute("role", "teacher");
        model.addAttribute("students", applications);
        model.addAttribute("sessions", sessionValues); // Add session values to model
        
        System.out.println("students "+applications);

        return "crewVersionModule/teacherViewApplications";
    }
    
    // Bulk approve/reject students
    @PostMapping("/approveRejectStudents")
    public String bulkApproveRejectStudents(@RequestParam("studentIds") List<Integer> studentIds,
                                            @RequestParam("action") String action, Model model) {
        // Print the received parameters 
        System.out.println("Received studentIds: " + studentIds);
        System.out.println("Received action: " + action);
        if (studentIds == null || studentIds.isEmpty()) {
            model.addAttribute("error", "Please select at least one student.");
            return "redirect:/tvpss-core/teacherViewApplication";
        }

        // Validate the action (approve or reject)
        if ("approve".equalsIgnoreCase(action)) {
            CrewService.updateStudentStatus(studentIds, "Approved");
            model.addAttribute("success", "Selected students have been approved.");
        } else if ("reject".equalsIgnoreCase(action)) {
            CrewService.updateStudentStatus(studentIds, "Rejected");
            model.addAttribute("success", "Selected students have been rejected.");
        } else {
            model.addAttribute("error", "Invalid action.");
        }

        return "redirect:/teacherViewApplication";
    }
    
    
    
    @GetMapping("/districtViewApplication")
    public String districtViewApplication(HttpSession session, Model model) {
        System.out.println("I'm in");

        // Get the userID from the session
        //Integer userID = (Integer) session.getAttribute("userID");
        Integer userID = 1;
        if (userID == null) {
            throw new IllegalStateException("User is not logged in.");
        }

        // Fetch applications using the service
        List<Map<String, Object>> applications = CrewService.getTVPSSVersionApplication(userID);
        
        // Add role and dynamic data to the model
        model.addAttribute("role", "teacher");
        model.addAttribute("versionApplications", applications);
        
        System.out.println("versionApplications "+applications);


        return "crewVersionModule/districtViewApplication";
    }

	 @GetMapping("/districtMainView")
	 public String districtMainView(Model model) {
		    // Step 1: Call the service to get schools and users
	        //Integer userID = (Integer) session.getAttribute("userID");
	        Integer userID = 1;
	        if (userID == null) {
	            throw new IllegalStateException("User is not logged in.");
	        }
	        Integer districtID = CrewService.getDistrictIdByUserId(userID);
	        System.out.println("District ID: " + districtID);
	        
		    Map<String, Object> result = CrewService.getSchoolsAndUsersByDistrict(districtID);

		    // Step 2: Extract the list of schools and users from the result
		    List<School> schools = (List<School>) result.get("schools");
		    List<UserModel> users = (List<UserModel>) result.get("users");

		    // Step 3: Extract the districtIDs from the schools
		    List<Integer> districtIDs = new ArrayList<>();
		    for (School school : schools) {
		    	System.out.println("districtID "+ school.getDistrictID());
		        districtIDs.add(school.getDistrictID());
		    }

		    // Step 4: Get district names based on districtIDs by calling the CrewService method
		    List<String> districtNames = CrewService.getCrewNamesByDistrictIDs(districtIDs);

		    // Step 5: Create a Map for easy lookup of Users by schoolID
		    Map<Integer, UserModel> userMap = new HashMap<>();
		    for (UserModel user : users) {
		    	System.out.println("user "+user.getName());
		        userMap.put((int) user.getSchoolID(), user);  // Map schoolID to the corresponding user
		    }

		    // Step 6: Add the user information to each school in the model (if needed)
		    model.addAttribute("schools", schools);  // Schools list
		    model.addAttribute("district", districtNames);  // District names list
		    model.addAttribute("userMap", userMap);  // User map to be accessed in the view

		    // Step 7: Return the view name to render
		    return "crewVersionModule/districtMainView";  // Path to the HTML template
		}
	 
	 
	 //STATE'S CONTROLLER METHODS
	 @GetMapping("/stateMainView")
	 public String getSchoolsAndDistrictDetails(Model model) {
		    // Step 1: Call the service to get schools and users
		    Map<String, Object> result = CrewService.getSchoolsAndUsers();

		    // Step 2: Extract the list of schools and users from the result
		    List<School> schools = (List<School>) result.get("schools");
		    List<UserModel> users = (List<UserModel>) result.get("users");

		    // Step 3: Extract the districtIDs from the schools
		    List<Integer> districtIDs = new ArrayList<>();
		    for (School school : schools) {
		    	System.out.println("districtID "+ school.getDistrictID());
		        districtIDs.add(school.getDistrictID());
		    }

		    // Step 4: Get district names based on districtIDs by calling the CrewService method
		    List<String> districtNames = CrewService.getCrewNamesByDistrictIDs(districtIDs);

		    // Step 5: Create a Map for easy lookup of Users by schoolID
		    Map<Integer, UserModel> userMap = new HashMap<>();
		    for (UserModel user : users) {
		    	System.out.println("user "+user.getName());
		        userMap.put((int) user.getSchoolID(), user);  // Map schoolID to the corresponding user
		    }

		    // Step 6: Add the user information to each school in the model (if needed)
		    model.addAttribute("schools", schools);  // Schools list
		    model.addAttribute("district", districtNames);  // District names list
		    model.addAttribute("userMap", userMap);  // User map to be accessed in the view

		    // Step 7: Return the view name to render
		    return "crewVersionModule/stateMainView";  // Path to the HTML template
		}

	 
	 @GetMapping("/viewCrewSchool/{id}")
	 public String viewSchool(@PathVariable("id") int schoolId,
			 HttpSession session,
			 Model model) {
	     System.out.println("View school with ID: " + schoolId);
		 String viewPath = "";
		 //String role = (String) session.getAttribute("role");
		 String role = "District";
		 if ("District".equalsIgnoreCase(role)) {
		        viewPath = "/districtMainView";
		        System.out.println("view is "+ viewPath);
		    } else if ("State".equalsIgnoreCase(role)) {
		        viewPath = "/stateMainView";
		    } 
	     // Fetch school data using the service method
	     Map<String, Object> schoolData = CrewService.getTVPSSCrewVersionInfo(schoolId);

	     if (schoolData == null || schoolData.isEmpty()) {
	         model.addAttribute("error", "School not found");
	         return "crewVersionModule/stateViewMore";
	     }
	     System.out.println("school data "+ schoolData);

	     // Add school data to the model
	     model.addAttribute("school", schoolData);
	     model.addAttribute("viewPath", viewPath);

	     return "crewVersionModule/viewMore"; // Return to the view page
	 }
	 
	 @PostMapping("/approveApplication")
	 public String approveApplication(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
	     // Assuming you have a service method to update the application status
	     boolean isUpdated = CrewService.updateApproveApplicationTvpssVersion(id);

	     // Log the ID and the operation
	     System.out.println("Application ID: " + id);
	     System.out.println("Status updated to 'Approved'");

	     // Set a redirect attribute to indicate success or failure
	     if (isUpdated) {
	         redirectAttributes.addAttribute("status", "success");
	     } else {
	         redirectAttributes.addAttribute("status", "error");
	     }

	     // Redirect to the applications page or another view after processing
	     return "redirect:/districtViewApplication";  // Adjust as needed
	 }
	 
	 @PostMapping("/rejectApplication")
	 public String rejectApplication(@RequestParam("id") int id,
			 @RequestParam("rejectReason") String rejectReason,
			 RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
	     // Assuming you have a service method to update the application status
	     boolean isUpdated = CrewService.updateRejectedApplicationTvpssVersion(id,rejectReason);
	     System.out.println("result is "+ isUpdated);
	     // Log the ID and the operation
	     System.out.println("Application ID: " + id);
	     System.out.println("Status updated to 'Approved'");

	     // Set a redirect attribute to indicate success or failure
	     if (isUpdated) {
	    	 
	         redirectAttributes.addAttribute("status", "success");
	     } else {
	         redirectAttributes.addAttribute("status", "error");
	     }

	     // Redirect to the applications page or another view after processing
	     return "redirect:/districtViewApplication";  // Adjust as needed
	 }



}
