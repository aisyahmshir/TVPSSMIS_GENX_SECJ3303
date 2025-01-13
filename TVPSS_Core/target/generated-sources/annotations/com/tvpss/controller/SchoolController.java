package com.tvpss.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mysql.cj.jdbc.Blob;
import com.tvpss.model.CrewModel;
import com.tvpss.model.School;
import com.tvpss.model.SchoolModel;
import com.tvpss.model.UserModel;
import com.tvpss.service.CrewService;
import com.tvpss.service.EmailService;
import com.tvpss.service.SchoolService;

@Controller
public class SchoolController{
    private School school;

	// Display the Add School Form
	
    @GetMapping("/addSchool")
    public String showAddSchoolForm(HttpSession session, Model model) {
		String role = (String) session.getAttribute("role");
	    
	    if("TEACHER".equalsIgnoreCase(role) == false) {
	    	return "UserManagement/login"; 
	    }
        Integer userId = (Integer) session.getAttribute("id"); 
        model.addAttribute("allDistricts", SchoolService.getAllDistricts()); // Fetch districts for dropdown
        model.addAttribute("userId", userId); // Add userId to the model for use in the form if needed
        return "SchoolManagement/addSchoolForm"; // Return the name of the HTML template
    }

    @PostMapping("/addSchool")
    public String addSchool(@RequestParam("name") String name, 
                            @RequestParam("fullAddress") String fullAddress, 
                            @RequestParam("state") String state, 
                            @RequestParam("districtID") String districtIDStr, 
                            @RequestParam("contactNo") String contactNo,
                            @RequestParam("versionImageURL") String versionImageURL,
                            HttpSession session
                            ) { // Add userId as a parameter
    	Integer userId = (Integer) session.getAttribute("id");
        int districtID = Integer.parseInt(districtIDStr);
        
        School school = new School(name, fullAddress, state, districtID, contactNo, versionImageURL);

        // Handle districtID conversion
        if (districtIDStr != null && !districtIDStr.isEmpty()) {
            try {
                districtID = Integer.parseInt(districtIDStr);
                school.setDistrictID(districtID);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Handle invalid district ID
                return "redirect:/error"; // Redirect to error page
            }
        } else {
            return "redirect:/error"; // Redirect to error page if districtID is missing
        }

        // Add the school to the database and retrieve the school object with ID
        School addedSchool = SchoolService.addSchool(school, userId); // Pass userId to the service method
        if (addedSchool != null) {
            return "redirect:/teacherSchoolView"; // Redirect to success page
        } else {
            return "redirect:/error"; // Redirect to error page if adding failed
        }
    }
	
	@GetMapping("/editSchool/{schoolId}")
	public String showEditSchoolForm(@PathVariable int schoolId, Model model,HttpSession session) {
		String role = (String) session.getAttribute("role");
	    
	    if("TEACHER".equalsIgnoreCase(role) == false) {
	    	return "UserManagement/login"; 
	    }
	    // Fetch the school details using the schoolId
	    School schoolDetail = SchoolService.getSchoolDetailsBySchoolID(schoolId);
	    
	    // Check if the school details were found
	    if (schoolDetail != null) {
	        model.addAttribute("schoolDetail", schoolDetail);
	        // Fetch all districts for the dropdown if needed
	        Map<Integer, String> allDistricts = SchoolService.getAllDistricts();
	        model.addAttribute("allDistricts", allDistricts);
	        return "SchoolManagement/editSchoolInfo";
	    } else {
	        model.addAttribute("errorMessage", "School not found.");
	        return "redirect:/error"; // Redirect to error page if school not found
	    }
	}
	
	@PostMapping("/editSchool/{schoolId}")
	public String editSchool(
	        @RequestParam("name") String name, 
	        @RequestParam("fullAddress") String fullAddress, 
	        @RequestParam("state") String state, 
	        @RequestParam("districtID") String districtIDStr, 
	        @RequestParam("contactNo") String contactNo,
	        @RequestParam("versionImageURL") String versionImageURL,
	        RedirectAttributes redirectAttributes, 
	        @PathVariable int schoolId) {
	    
	    System.out.println("Editing school: " + name); // Log the school object

	    // Create a new School object and set its properties
	    School school = new School();
	    school.setSchoolID(schoolId); // Set the schoolID from the path variable
	    school.setName(name);
	    school.setFullAddress(fullAddress);
	    school.setState(state);
	    
	    // Handle districtID conversion
	    if (districtIDStr != null && !districtIDStr.isEmpty()) {
	        try {
	            int districtID = Integer.parseInt(districtIDStr);
	            school.setDistrictID(districtID);
	        } catch (NumberFormatException e) {
	            e.printStackTrace(); // Handle invalid district ID
	            redirectAttributes.addFlashAttribute("errorMessage", "Invalid district ID.");
	            return "redirect:/teacherSchoolView"; // Redirect back to the view
	        }
	    } else {
	        redirectAttributes.addFlashAttribute("errorMessage", "District ID is required.");
	        return "redirect:/teacherSchoolView"; // Redirect back to the view
	    }

	    school.setContactNo(contactNo);
	    school.setVersionImageURL(versionImageURL);

	    // Call the service method to update the school
	    boolean isUpdated = SchoolService.editSchool(school);
	    if (isUpdated) {
	        redirectAttributes.addFlashAttribute("message", "School updated successfully!");
	        return "redirect:/teacherSchoolView"; // Redirect to success page
	    } else {
	        redirectAttributes.addFlashAttribute("errorMessage", "Failed to update school.");
	        return "redirect:/teacherSchoolView"; // Redirect back to the edit form
	    }
	}
	
	@GetMapping("/teacherSchoolView")
	public String manageSchoolInfo(HttpSession session, Model model) {
		String role = (String) session.getAttribute("role");
	    
	    if("TEACHER".equalsIgnoreCase(role) == false) {
	    	return "UserManagement/login"; 
	    }
		int schoolId = (int) session.getAttribute("schoolID");
	    System.out.println("Fetching details for school ID: " + schoolId);

	    // Fetch the school details using the schoolId
	    School schoolDetail = SchoolService.getSchoolDetailsBySchoolID(schoolId);
	    
	    // Check if the school details were found
	    if (schoolDetail != null) {
	        model.addAttribute("schoolDetail", schoolDetail);

	        // Fetch all districts
	        Map<Integer, String> allDistricts = SchoolService.getAllDistricts();
	        model.addAttribute("allDistricts", allDistricts);
	        
	        // Get the district name based on the districtID of the school
	        String districtName = allDistricts.get(schoolDetail.getDistrictID());
	        model.addAttribute("districtName", districtName);
	        
	        UserModel teacher = SchoolService.getTeacherBySchoolId(schoolId);
	        if (teacher != null) {
	            model.addAttribute("teacherName", teacher.getName());
	        } else {
	            model.addAttribute("teacherName", "Not Assigned");
	        }
	        
	        // Fetch all studios
	        Map<Integer, Integer> allStudios = SchoolService.getAllStudios();
	        model.addAttribute("allStudios", allStudios);
	        
	        // Get the studio level based on the studioID of the school
	            Integer studioLevel = allStudios.get(schoolDetail.getStudioID());
	            model.addAttribute("studioLevel", studioLevel != null ? studioLevel : 0);

	    } else {
	        model.addAttribute("errorMessage", "School not found.");
	    }

	    return "SchoolManagement/teacherSchoolView"; // Return the view name
	}
	
	@GetMapping("/districtSchoolsView")
	public String districtViewSchools( Model model, HttpSession session) {
		String role = (String) session.getAttribute("role");
	    
		if (!"DISTRICT OFFICER".equalsIgnoreCase(role) && !"STATE OFFICER".equalsIgnoreCase(role)) {
		    return "UserManagement/login"; 
		}

			int districtId = (int) session.getAttribute("districtID");
	    session.setAttribute("currentDistrictId", districtId);
	    // Fetch schools for the given district ID
	    Map<String, Object> result = SchoolService.getSchoolsByDistrictId(districtId);
	    
	    // Extract the list of schools from the result map
	    List<School> schoolsList = (List<School>) result.get("schools");
	    
	 // Fetch all districts and studios once
	    Map<Integer, String> allDistricts = SchoolService.getAllDistricts();
	    Map<Integer, Integer> allStudios = SchoolService.getAllStudios();
	    
	    // Prepare lists to hold district names and studio levels
	    List<String> districtNames = new ArrayList<>();
	    List<Integer> studioLevels = new ArrayList<>();
	    List<String> teacherNames = new ArrayList<>();

	    List<Integer> schoolIds = schoolsList.stream()
                .map(School::getSchoolID)
                .collect(Collectors.toList());
	    
	    // Iterate through the schools to get district names and studio levels
	    for (School school : schoolsList) {
	        // Get the district name based on the districtID of the school
	        String districtName = allDistricts.get(school.getDistrictID());
	        districtNames.add(districtName != null ? districtName : "Unknown District");

	        // Get the studio level based on the studioID of the school
	        Integer studioLevel = allStudios.get(school.getStudioID());
	        studioLevels.add(studioLevel != null ? studioLevel : 0); // Default to 0 if not found
	        
	        UserModel teacher = SchoolService.getTeacherBySchoolId(school.getSchoolID());
	        if (teacher != null) {
	            teacherNames.add(teacher.getName());
	        } else {
	            teacherNames.add("Not Assigned");
	        }
	    }

	    // Add the lists to the model
	    model.addAttribute("schoolsList", schoolsList);
	    model.addAttribute("districtNames", districtNames);
	    model.addAttribute("studioLevels", studioLevels);
	    model.addAttribute("teacherNames", teacherNames);
	    
	    return "SchoolManagement/districtSchoolsView"; // Return the view name
	}
	
	@GetMapping("/schoolDetail/{schoolId}")
    public String districtViewSchoolDetail(@PathVariable("schoolId") int schoolId, Model model, HttpSession session) {
		String role = (String) session.getAttribute("role");
	    
		if (!"DISTRICT OFFICER".equalsIgnoreCase(role) && !"STATE OFFICER".equalsIgnoreCase(role)) {
		    return "UserManagement/login"; 
		}
	    System.out.println("Fetching details for school ID: " + schoolId);

	    session.setAttribute("currentSchoolId", schoolId);
	    // Fetch the school details using the schoolId
	    School schoolDetail = SchoolService.getSchoolDetailsBySchoolID(schoolId);
	    
	    // Check if the school details were found
	    if (schoolDetail != null) {
	        model.addAttribute("schoolDetail", schoolDetail);

	        // Fetch all districts
	        Map<Integer, String> allDistricts = SchoolService.getAllDistricts();
	        model.addAttribute("allDistricts", allDistricts);
	        
	        // Get the district name based on the districtID of the school
	        String districtName = allDistricts.get(schoolDetail.getDistrictID());
	        model.addAttribute("districtName", districtName);
	        
	        UserModel teacher = SchoolService.getTeacherBySchoolId(schoolId);
	        if (teacher != null) {
	            model.addAttribute("teacherName", teacher.getName());
	        } else {
	            model.addAttribute("teacherName", "Not Assigned");
	        }
	        
	        Map<Integer, Integer> allStudios = SchoolService.getAllStudios();
	        model.addAttribute("allStudios", allStudios);
	        
	        // Get the district name based on the districtID of the school
	        Integer studioLevel = allStudios.get(schoolDetail.getStudioID());
	        model.addAttribute("studioLevel", studioLevel != null ? studioLevel : 0);
	    } else {
	        model.addAttribute("errorMessage", "School not found.");
	    }
        return "SchoolManagement/schoolDetailView";
    }
	
	@GetMapping("/stateDistrictsInfo")
    public String viewDistrictsInfo(Model model,HttpSession session) {
		String role = (String) session.getAttribute("role");
	    
		if ("STATE OFFICER".equalsIgnoreCase(role) == false) {
		    return "UserManagement/login"; 
		}
        List<Map<String, Object>> districts = SchoolService.getDistrictsWithDetails();
        model.addAttribute("districts", districts);

        return "SchoolManagement/stateDistrictsInfoView"; // The name of the HTML/Thymeleaf template
    }

    @GetMapping("/addEquip")
    public String showAddEquipmentForm(HttpSession session, Model model) {
		String role = (String) session.getAttribute("role");
	    
		if ("TEACHER".equalsIgnoreCase(role) == false) {
		    return "UserManagement/login"; 
		}
		int schoolId = (int) session.getAttribute("schoolID");
        List<Map<String, Object>> equipmentList = SchoolService.getAllEquipment();

        // Add the equipment list and school ID to the model
        model.addAttribute("equipmentList", equipmentList);
        model.addAttribute("schoolDetail", SchoolService.getSchoolDetailsBySchoolID(schoolId)); // Fetch school details if needed

        return "SchoolManagement/addEquipForm"; // Return the name of the Thymeleaf template
    }
    
 @PostMapping("manageEquipment/{schoolId}")
    public String addNewEquipment(@PathVariable int schoolId, 
                                   @RequestParam Map<String, String> equipmentStatus, 
                                   @RequestParam String imagesLink, 
                                   RedirectAttributes redirectAttributes) {
        SchoolService.addNewEquipment(schoolId, equipmentStatus, imagesLink);
        redirectAttributes.addFlashAttribute("message", "Equipment availability added successfully!");
        return "redirect:/manageEquipment"; // Redirect to the manage equipment page
    }

 
	@GetMapping("/editEquipment/{schoolId}")
	public String editEquipment(@PathVariable int schoolId, Model model,HttpSession session) {
		String role = (String) session.getAttribute("role");
	    
		if ("TEACHER".equalsIgnoreCase(role) == false) {
		    return "UserManagement/login"; 
		}
	    // Fetch studio and equipment details
	    Map<String, Object> studioAndEquipmentDetails = SchoolService.getStudioAndEquipmentDetails(schoolId);
	    School schoolDetail = SchoolService.getSchoolDetailsBySchoolID(schoolId);
	    
	    Map<Integer, String> allDistricts = SchoolService.getAllDistricts();
        model.addAttribute("allDistricts", allDistricts);
        
        // Get the district name based on the districtID of the school
        String districtName = allDistricts.get(schoolDetail.getDistrictID());
        model.addAttribute("districtName", districtName);
        
        UserModel teacher = SchoolService.getTeacherBySchoolId(schoolId);
        if (teacher != null) {
            model.addAttribute("teacherName", teacher.getName());
        } else {
            model.addAttribute("teacherName", "Not Assigned");
        }
        
        Map<Integer, Integer> allStudios = SchoolService.getAllStudios();
        model.addAttribute("allStudios", allStudios);
        
        // Get the district name based on the districtID of the school
        Integer studioLevel = allStudios.get(schoolDetail.getStudioID());
        model.addAttribute("studioLevel", studioLevel != null ? studioLevel : 0);
	    // Add studio level and equipment list to the model
	    model.addAttribute("studioLevel", studioAndEquipmentDetails.get("studioLevel"));
	    model.addAttribute("equipmentList", studioAndEquipmentDetails.get("equipmentList"));
        model.addAttribute("imagesLink", studioAndEquipmentDetails.get("imagesLink"));
	    // Fetch school details to display
	    model.addAttribute("schoolDetail", schoolDetail);

	    return "SchoolManagement/editEquipPage";
	}
	
	 @PostMapping("/save/{schoolId}")
	    public String editEquipment(@PathVariable int schoolId, 
	                                   @RequestParam Map<String, String> equipmentStatus, 
	                                   @RequestParam(value = "imagesLink", required = false) String imagesLink, 
	                                   RedirectAttributes redirectAttributes) {
	        SchoolService.editEquipment(schoolId, equipmentStatus, imagesLink);
	        redirectAttributes.addFlashAttribute("message", "Equipment availability added successfully!");
	        return "redirect:/manageEquipment"; // Redirect to the manage equipment page
	    }

	@GetMapping("/manageEquipment")
	public String manageEquipment(HttpSession session, Model model) {
		String role = (String) session.getAttribute("role");
	    
		if (!"TEACHER".equalsIgnoreCase(role) && !"DISTRICT OFFICER".equalsIgnoreCase(role) && !"STATE OFFICER".equalsIgnoreCase(role)) {
		    return "UserManagement/login"; 
		}

		int schoolId = (int) session.getAttribute("schoolID");
		System.out.println("haha "+schoolId);
	    // Fetch studio and equipment details
	    Map<String, Object> studioAndEquipmentDetails = SchoolService.getStudioAndEquipmentDetails(schoolId);
	    School schoolDetail = SchoolService.getSchoolDetailsBySchoolID(schoolId);
	    
	    Map<Integer, String> allDistricts = SchoolService.getAllDistricts();
        model.addAttribute("allDistricts", allDistricts);
        
        // Get the district name based on the districtID of the school
        String districtName = allDistricts.get(schoolDetail.getDistrictID());
        model.addAttribute("districtName", districtName);
        
        UserModel teacher = SchoolService.getTeacherBySchoolId(schoolId);
        if (teacher != null) {
            model.addAttribute("teacherName", teacher.getName());
        } else {
            model.addAttribute("teacherName", "Not Assigned");
        }
        
        Map<Integer, Integer> allStudios = SchoolService.getAllStudios();
        model.addAttribute("allStudios", allStudios);
        
        // Get the district name based on the districtID of the school
        int studioLevel = allStudios.get(schoolDetail.getStudioID());
        model.addAttribute("studioLevel", studioLevel);
	    // Add studio level and equipment list to the model
	    model.addAttribute("studioLevel", studioAndEquipmentDetails.get("studioLevel"));
	    model.addAttribute("equipmentList", studioAndEquipmentDetails.get("equipmentList"));
        model.addAttribute("imagesLink", studioAndEquipmentDetails.get("imagesLink"));
	    // Fetch school details to display
	    model.addAttribute("schoolDetail", schoolDetail);
	    
	    String studioLevelStatus = (String) studioAndEquipmentDetails.get("studioLevelStatus");
	    model.addAttribute("studioLevelStatus", studioLevelStatus);

	    return "SchoolManagement/teacherManageEquip"; // Return the view name
	}
	
	@GetMapping("/districtSchoolsStudio")
	public String viewDistrictSchools( Model model,HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (!"DISTRICT OFFICER".equalsIgnoreCase(role) && !"STATE OFFICER".equalsIgnoreCase(role)) {
		    return "UserManagement/login"; 
		}

		int districtId = (int) session.getAttribute("districtID");
		System.out.println("haha "+districtId);
	    Map<String, Object> result = SchoolService.getSchoolsStudio(districtId);
	    
	    model.addAttribute("schoolDetails", result.get("schoolDetails"));
	  	    
	    // Retrieve and add studio level status to the model
	    String studioLevelStatus = (String) result.get("studioLevelStatus");
	    model.addAttribute("studioLevelStatus", studioLevelStatus);
	    
	    return "SchoolManagement/districtManageStudio";
	}
	
	@GetMapping("/districtManageStudioDetail/{schoolId}")
	public String viewDistrictManageStudioDetails(@PathVariable int schoolId, Model model,HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (!"DISTRICT OFFICER".equalsIgnoreCase(role) && !"STATE OFFICER".equalsIgnoreCase(role)) {
		    return "UserManagement/login"; 
		}
	    // Fetch studio and equipment details
	    Map<String, Object> studioAndEquipmentDetails = SchoolService.getStudioAndEquipmentDetails(schoolId);
	    School schoolDetail = SchoolService.getSchoolDetailsBySchoolID(schoolId);
	    
	    Map<Integer, String> allDistricts = SchoolService.getAllDistricts();
	    model.addAttribute("allDistricts", allDistricts);
	    
	    // Get the district name based on the districtID of the school
	    String districtName = allDistricts.get(schoolDetail.getDistrictID());
	    model.addAttribute("districtName", districtName);
	    
	    UserModel teacher = SchoolService.getTeacherBySchoolId(schoolId);
	    if (teacher != null) {
	        model.addAttribute("teacherName", teacher.getName());
	    } else {
	        model.addAttribute("teacherName", "Not Assigned");
	    }
	    
	    Map<Integer, Integer> allStudios = SchoolService.getAllStudios();
	    model.addAttribute("allStudios", allStudios);
	    
	    // Get the studio level from the studioAndEquipmentDetails
	    int studioLevel = (int) studioAndEquipmentDetails.get("studioLevel");
	    model.addAttribute("studioLevel", studioLevel);
	    
	    // Retrieve and add studio level status to the model
	    String studioLevelStatus = (String) studioAndEquipmentDetails.get("studioLevelStatus");
	    model.addAttribute("studioLevelStatus", studioLevelStatus);
	    
	    
	    // Add equipment list and images link to the model
	    model.addAttribute("equipmentList", studioAndEquipmentDetails.get("equipmentList"));
	    model.addAttribute("imagesLink", studioAndEquipmentDetails.get("imagesLink"));
	   	    
	    
	    // Fetch school details to display
	    model.addAttribute("schoolDetail", schoolDetail);
	    
	    return "SchoolManagement/districtManageStudioDetail"; // Return the view name
	}
	
	@PostMapping("/approved/{id}")
	public String approveUpgrade(@PathVariable int id, RedirectAttributes redirectAttributes) {
	    // Update the studioLevelStatus to "Approved" in the database
	    SchoolService.updateStudioLevelStatus(id, "Approved");
	    
	    // Automatically update the studio level when approved
	    SchoolService.updateStudioLevel(id);
	    
	    redirectAttributes.addFlashAttribute("message", "Upgrade request approved successfully.");
	    return "redirect:/districtManageStudioDetail/" + id;
	}
 
	@PostMapping("/rejected/{id}")
	public String declineUpgrade(@PathVariable int id, RedirectAttributes redirectAttributes) {
	    // Update the studioLevelStatus to "Rejected" in the database
	    SchoolService.updateStudioLevelStatus(id, "Rejected");
	    
	    // Automatically update the studio level when approved
	    SchoolService.updateStudioLevel(id);
	    
	    redirectAttributes.addFlashAttribute("message", "Upgrade request declined successfully.");
	    return "redirect:/districtManageStudioDetail/" + id;
	}
	
	@GetMapping("/stateDistrictsStudioInfo")
    public String viewDistrictsStudioInfo(Model model,HttpSession session) {
		String role = (String) session.getAttribute("role");
		if ("STATE OFFICER".equalsIgnoreCase(role) ==false) {
		    return "UserManagement/login"; 
		}
        List<Map<String, Object>> districts = SchoolService.getDistrictsWithDetails();
        model.addAttribute("districts", districts);

        return "SchoolManagement/stateDistrictsStudio"; // The name of the HTML/Thymeleaf template
    }

}