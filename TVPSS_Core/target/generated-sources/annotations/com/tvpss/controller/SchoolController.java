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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tvpss.model.CrewModel;

@Controller
public class SchoolController {
	
	private List<String> schools = new ArrayList<>();

    // Display the Add School Form
	@GetMapping("/addSchool/{id}")
	public String addSchoolForm(@PathVariable int id, Model model) {
	    // Fetch the school data based on the ID
	    Map<String, Object> schoolDetail = getSchoolById(id); // Replace with actual data fetching logic

	    // Add school data to the model
	    model.addAttribute("schoolDetail", schoolDetail);
	    return "SchoolManagement/addSchoolForm";
	}


	@GetMapping("/districtSchoolsView")
	public String districtSchool(Model model) {
	    System.out.println("I'm in");

        // Add dynamic data for school list
        List<Map<String, Object>> schoolsList = new ArrayList<>();

     // School 1
        Map<String, Object> school1 = new HashMap<>();
        school1.put("id", 1);
        school1.put("schoolName", "SK Pagoh");
        school1.put("teacherInCharge", "Ms. Syafidah");
        school1.put("schoolPhone", "07-5562577");
        school1.put("schoolAddress", "Sekolah Kebangsaan Pagoh, KM 33, Pekan Pagoh, 84600 Pagoh, Johor");
        school1.put("postcode", 84600);
        school1.put("district", "Pagoh");
        school1.put("tvpssVersion", "2");
        school1.put("studioLevel", "2");
        school1.put("schoolLogo", "path/to/logo.png");
        schoolsList.add(school1);
        
        // School 2
        Map<String, Object> school2 = new HashMap<>();
        school2.put("id", 2);
        school2.put("schoolName", "SK Batu Pahat");
        school2.put("teacherInCharge", "Ms. Afiqah");
        school2.put("schoolPhone", "07-5562577");
        school2.put("schoolAddress", "Sekolah Kebangsaan Batu Pahat, KM4, 83000 Batu Pahat, Johor ");
        school2.put("postcode", 83000);
        school2.put("district", "Batu Pahat");
        school2.put("tvpssVersion", "3");
        school2.put("studioLevel", "2");
        school2.put("schoolLogo", "path/to/logo.png");
        schoolsList.add(school2);

        // Add the list of schools to the model
        model.addAttribute("schoolsList", schoolsList);

        return "SchoolManagement/districtSchoolsView";
	}
	
	@GetMapping("/stateDistrictsInfo")
    public String viewDistrictsInfo(Model model) {
		List<Map<String, Object>> districts = new ArrayList<>();

        //District 1
        Map<String, Object> district1 = new HashMap<>();
        district1.put("dID", 1);
        district1.put("districtName", "Batu Pahat");
        district1.put("personInCharge", "Mr. Zulaifiq");
        district1.put("totalSchools", "3");
        districts.add(district1);
        
        //District 2
        Map<String, Object> district2 = new HashMap<>();
        district2.put("dID", 2);
        district2.put("districtName", "Pagoh");
        district2.put("personInCharge", "Ms. Anita");
        district2.put("totalSchools", "5");
        districts.add(district2);
        
      //District 3
        Map<String, Object> district3 = new HashMap<>();
        district3.put("dID", 3);
        district3.put("districtName", "Kota Tinggi");
        district3.put("personInCharge", "Mr. Afiq");
        district3.put("totalSchools", "3");
        districts.add(district3);
        
        // Add the map to the model
        model.addAttribute("districts", districts);

        return "SchoolManagement/stateDistrictsInfoView"; // The name of the HTML/Thymeleaf template
    }
	
	@GetMapping("/stateSchoolsView/{dID}")
	public String viewDistrictSchools(@PathVariable int dID, Model model) {
		List<Map<String, Object>> schoolsList = new ArrayList<>();

		// School 1
        Map<String, Object> school1 = new HashMap<>();
        school1.put("id", 1);
        school1.put("schoolName", "SK Pagoh");
        school1.put("teacherInCharge", "Ms. Syafidah");
        school1.put("schoolPhone", "07-5562577");
        school1.put("schoolAddress", "Sekolah Kebangsaan Pagoh, KM 33, Pekan Pagoh, 84600 Pagoh, Johor");
        school1.put("postcode", 84600);
        school1.put("district", "Pagoh");
        school1.put("tvpssVersion", "2");
        school1.put("studioLevel", "2");
        school1.put("schoolLogo", "path/to/logo.png");
        schoolsList.add(school1);
        
        // School 2
        Map<String, Object> school2 = new HashMap<>();
        school2.put("id", 2);
        school2.put("schoolName", "SK Batu Pahat");
        school2.put("teacherInCharge", "Ms. Afiqah");
        school2.put("schoolPhone", "07-5562577");
        school2.put("schoolAddress", "Sekolah Kebangsaan Batu Pahat, KM4, 83000 Batu Pahat, Johor ");
        school2.put("postcode", 83000);
        school2.put("district", "Batu Pahat");
        school2.put("tvpssVersion", "3");
        school2.put("studioLevel", "2");
        school2.put("schoolLogo", "path/to/logo.png");
        schoolsList.add(school2);

        // Add the list of schools to the model
        model.addAttribute("schoolsList", schoolsList);

	    return "SchoolManagement/stateSchoolsView";

	}
	
	@GetMapping("/teacherSchoolView/{id}")
    public String manageSchoolInfo(@PathVariable int id, Model model) {
        System.out.println("Fetching details for school ID: " + id);

        // Mock data for demonstration (replace with actual data retrieval from database or service)
        Map<String, Object> schoolDetail = new HashMap<>();
        schoolDetail.put("id", id);
        schoolDetail.put("schoolName", "SK PAGOH");
        schoolDetail.put("teacherInCharge", "PN. SYAFIDAH");
        schoolDetail.put("schoolPhone", "07-5562577");
        schoolDetail.put("schoolAddress", "SK PAGOH, KM 33, PEKAN PAGOH, 84600 PAGOH, JOHOR");
        schoolDetail.put("postcode", 81300);
        schoolDetail.put("district", "PAGOH");
        schoolDetail.put("tvpssVersion", "2");
        schoolDetail.put("studioLevel", "2");
        schoolDetail.put("schoolLogo", "path/to/logo.png"); // Example image path (update as needed)

        // Add school details to the model
        model.addAttribute("schoolDetail", schoolDetail);

        return "SchoolManagement/teacherSchoolView";
    }
	
	@GetMapping("/districtSchoolDetail/{id}")
    public String districtViewSchoolDetail(@PathVariable int id, Model model) {
        System.out.println("Fetching details for school ID: " + id);

        // Mock data for demonstration (replace with actual data retrieval from database or service)
        Map<String, Object> schoolDetail = new HashMap<>();
        schoolDetail.put("id", 1);
        schoolDetail.put("schoolName", "SK Pagoh");
        schoolDetail.put("teacherInCharge", "Ms. Syafidah");
        schoolDetail.put("schoolPhone", "07-5562577");
        schoolDetail.put("schoolAddress", "Sekolah Kebangsaan Pagoh, KM 33, Pekan Pagoh, 84600 Pagoh, Johor");
        schoolDetail.put("postcode", 84600);
        schoolDetail.put("district", "Pagoh");
        schoolDetail.put("tvpssVersion", "2");
        schoolDetail.put("studioLevel", "2");
        schoolDetail.put("schoolLogo", "path/to/logo.png");

        // Add school details to the model
        model.addAttribute("schoolDetail", schoolDetail);

        return "SchoolManagement/districtSchoolDetailView";
    }
	
	@GetMapping("/stateSchoolDetail/{id}")
    public String stateViewSchoolDetail(@PathVariable int id, Model model) {
        System.out.println("Fetching details for school ID: " + id);

        // Mock data for demonstration (replace with actual data retrieval from database or service)
        Map<String, Object> schoolDetail = new HashMap<>();
        schoolDetail.put("id", 1);
        schoolDetail.put("schoolName", "SK Pagoh");
        schoolDetail.put("teacherInCharge", "Ms. Syafidah");
        schoolDetail.put("schoolPhone", "07-5562577");
        schoolDetail.put("schoolAddress", "Sekolah Kebangsaan Pagoh, KM 33, Pekan Pagoh, 84600 Pagoh, Johor");
        schoolDetail.put("postcode", 84600);
        schoolDetail.put("district", "Pagoh");
        schoolDetail.put("tvpssVersion", "2");
        schoolDetail.put("studioLevel", "2");
        schoolDetail.put("schoolLogo", "path/to/logo.png");

        // Add school details to the model
        model.addAttribute("schoolDetail", schoolDetail);

        return "SchoolManagement/stateSchoolDetailView";
    }
	
	@GetMapping("/editSchool/{id}")
	public String editSchool(@PathVariable int id, Model model) {
	    // Fetch the school data based on the ID
	    Map<String, Object> schoolDetail = getSchoolById(id); // Replace with actual data fetching logic

	    // Add school data to the model
	    model.addAttribute("schoolDetail", schoolDetail);
	    return "SchoolManagement/editSchoolInfo";
	}

	private Map<String, Object> getSchoolById(int id) {
	    Map<String, Object> school = new HashMap<>();
	    school.put("id", 1);
        school.put("schoolName", "SK Pagoh");
        school.put("teacherInCharge", "Ms. Syafidah");
        school.put("schoolPhone", "07-5562577");
        school.put("schoolAddress", "Sekolah Kebangsaan Pagoh, KM 33, Pekan Pagoh, 84600 Pagoh, Johor");
        school.put("postcode", 84600);
        school.put("district", "Pagoh");
        school.put("tvpssVersion", "2");
        school.put("studioLevel", "2");
        school.put("schoolLogo", "path/to/logo.png");
        school.put("equipmentImages", "https://drive.google.com/your-link-here");
	    return school;
	}
	
/*EQUIPMENT*/
    // Display the Add School Form
    @GetMapping("/addEquipForm/{id}")
    public String showAddEquipForm(@PathVariable int id, Model model) {
    	// Mock school details
    			Map<String, Object> schoolDetail = getSchoolById(id); 

    		    // Mock equipment list
    		    List<Map<String, String>> equipmentList = List.of(
    		        Map.of("name", "TV Program/Show corner", "status", "Yes"),
    		        Map.of("name", "Editing Corner", "status", "Yes"),
    		        Map.of("name", "Smartphone", "status", "No"),
    		        Map.of("name", "External Mic", "status", "Yes"),
    		        Map.of("name", "Monopod", "status", "No"),
    		        Map.of("name", "Mobile Green Screen set", "status", "Yes"),
    		        Map.of("name", "Tripod", "status", "No"),
    		        Map.of("name", "Wireless Mic", "status", "Yes"),
    		        Map.of("name", "Mobile Lighting", "status", "Yes"),
    		        Map.of("name", "Mobile Lighting (3 Point)", "status", "New"),
    		        Map.of("name", "Camera", "status", "No"),
    		        Map.of("name", "Editing Software", "status", "Yes"),
    		        Map.of("name", "Permanent Green Screen", "status", "No"),
    		        Map.of("name", "Webcam", "status", "No")
    		    );

    		    model.addAttribute("schoolDetail", schoolDetail);
    		    model.addAttribute("equipmentList", equipmentList);
        return "SchoolManagement/addEquipForm"; // Name of your Thymeleaf template
    }
    
	@GetMapping("/manageStudioEquipment/{id}")
	public String manageStudioEquipment(@PathVariable int id, Model model) {
	    // Mock school details
	    Map<String, Object> schoolDetail = new HashMap<>();
	    schoolDetail.put("id", id);
	    schoolDetail.put("schoolName", "SK Pagoh");
	    schoolDetail.put("district", "Pagoh");
	    schoolDetail.put("teacherInCharge", "Pn. Syafidah");
	    schoolDetail.put("studioLevel", 2);
	    schoolDetail.put("equipmentImages", "https://drive.google.com/your-link-here"); // Example

	    // List of equipment
	    List<Map<String, String>> equipmentList = new ArrayList<>();
	    equipmentList.add(Map.of("name", "TV Program/Show Corner", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Editing Corner", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Smartphone", "status", "No"));
	    equipmentList.add(Map.of("name", "External Mic", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Monopod", "status", "No"));
	    equipmentList.add(Map.of("name", "Mobile Green Screen Set", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Tripod", "status", "No"));
	    equipmentList.add(Map.of("name", "Wireless Mic", "status", "No"));
	    equipmentList.add(Map.of("name", "Mobile Lighting", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Webcam", "status", "No"));
	    equipmentList.add(Map.of("name", "Mobile Lighting (3 Point)", "status", "No"));
	    equipmentList.add(Map.of("name", "Camera", "status", "No"));
	    equipmentList.add(Map.of("name", "Editing Software", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Permanent Green Screen", "status", "No"));

	    model.addAttribute("schoolDetail",schoolDetail);
	    model.addAttribute("equipmentList", equipmentList);

	    return "SchoolManagement/teacherManageEquipPage";
	}

	@GetMapping("/editEquipment/{id}")
	public String editEquipment(@PathVariable int id, Model model) {
	    // Mock school details
		Map<String, Object> schoolDetail = getSchoolById(id); 

	    // Mock equipment list
	    List<Map<String, String>> equipmentList = List.of(
	        Map.of("name", "TV Program/Show corner", "status", "Yes"),
	        Map.of("name", "Editing Corner", "status", "Yes"),
	        Map.of("name", "Smartphone", "status", "No"),
	        Map.of("name", "External Mic", "status", "Yes"),
	        Map.of("name", "Monopod", "status", "No"),
	        Map.of("name", "Mobile Green Screen set", "status", "Yes"),
	        Map.of("name", "Tripod", "status", "No"),
	        Map.of("name", "Wireless Mic", "status", "Yes"),
	        Map.of("name", "Mobile Lighting", "status", "Yes"),
	        Map.of("name", "Mobile Lighting (3 Point)", "status", "New"),
	        Map.of("name", "Camera", "status", "No"),
	        Map.of("name", "Editing Software", "status", "Yes"),
	        Map.of("name", "Permanent Green Screen", "status", "No"),
	        Map.of("name", "Webcam", "status", "No")
	    );

	    model.addAttribute("schoolDetail", schoolDetail);
	    model.addAttribute("equipmentList", equipmentList);

	    return "SchoolManagement/editEquipPage";
	}

	@GetMapping("/districtManageStudio")
    public String districtManageStudio(Model model) {
		List<Map<String, Object>> school = new ArrayList<>();

        //District 1
        Map<String, Object> school1 = new HashMap<>();
        school1.put("id", 1);
        school1.put("schoolName", "SK Pagoh");
        school1.put("studioLevel", "2");
        school1.put("tvCorner", "Yes");
        school1.put("editCorner", "Yes");
        school1.put("equipments", "6");
        school.add(school1);
        
      //District 1
        Map<String, Object> school2 = new HashMap<>();
        school2.put("id", 2);
        school2.put("schoolName", "SK Pekan Pagoh");
        school2.put("studioLevel", "2");
        school2.put("tvCorner", "No");
        school2.put("editCorner", "Yes");
        school2.put("equipments", "6");
        school.add(school2);
        
        // Add the map to the model
        model.addAttribute("school", school);

        return "SchoolManagement/districtManageStudio"; // The name of the HTML/Thymeleaf template
    }
	
	@GetMapping("/districtManageStudioDetail/{id}")
	public String manageStudioDetail(@PathVariable int id, Model model) {
	    // Mock school details
	    Map<String, Object> schoolDetail = new HashMap<>();
	    schoolDetail.put("id", 1);
	    schoolDetail.put("schoolName", "SK Pagoh");
	    schoolDetail.put("district", "Pagoh");
	    schoolDetail.put("teacherInCharge", "Ms. Syafidah");
	    schoolDetail.put("studioLevel", 2);
	    schoolDetail.put("equipmentImages", "https://drive.google.com/your-link-here"); // Example

	    // List of equipment
	    List<Map<String, String>> equipmentList = new ArrayList<>();
	    equipmentList.add(Map.of("name", "TV Program/Show Corner", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Editing Corner", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Smartphone", "status", "No"));
	    equipmentList.add(Map.of("name", "External Mic", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Monopod", "status", "No"));
	    equipmentList.add(Map.of("name", "Mobile Green Screen Set", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Tripod", "status", "No"));
	    equipmentList.add(Map.of("name", "Wireless Mic", "status", "No"));
	    equipmentList.add(Map.of("name", "Mobile Lighting", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Webcam", "status", "No"));
	    equipmentList.add(Map.of("name", "Mobile Lighting (3 Point)", "status", "New"));
	    equipmentList.add(Map.of("name", "Camera", "status", "No"));
	    equipmentList.add(Map.of("name", "Editing Software", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Permanent Green Screen", "status", "No"));
	    
	    // Check for "New" status
	    boolean hasPendingApproval = equipmentList.stream()
	            .anyMatch(equipment -> "New".equals(equipment.get("status")));

	    model.addAttribute("schoolDetail",schoolDetail);
	    model.addAttribute("equipmentList", equipmentList);
	    model.addAttribute("hasPendingApproval", hasPendingApproval); // Add the flag

	    return "SchoolManagement/districtManageStudioDetail";
	}
	
	@GetMapping("/approved/{id}")
	public String approvedStudio(@PathVariable int id, Model model) {
	    // Mock school details
	    Map<String, Object> schoolDetail = new HashMap<>();
	    schoolDetail.put("id", 1);
	    schoolDetail.put("schoolName", "SK Pagoh");
	    schoolDetail.put("district", "Pagoh");
	    schoolDetail.put("teacherInCharge", "Ms. Syafidah");
	    schoolDetail.put("studioLevel", 3);
	    schoolDetail.put("equipmentImages", "https://drive.google.com/your-link-here"); // Example

	    // List of equipment
	    List<Map<String, String>> equipmentList = new ArrayList<>();
	    equipmentList.add(Map.of("name", "TV Program/Show Corner", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Editing Corner", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Smartphone", "status", "No"));
	    equipmentList.add(Map.of("name", "External Mic", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Monopod", "status", "No"));
	    equipmentList.add(Map.of("name", "Mobile Green Screen Set", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Tripod", "status", "No"));
	    equipmentList.add(Map.of("name", "Wireless Mic", "status", "No"));
	    equipmentList.add(Map.of("name", "Mobile Lighting", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Webcam", "status", "No"));
	    equipmentList.add(Map.of("name", "Mobile Lighting (3 Point)", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Camera", "status", "No"));
	    equipmentList.add(Map.of("name", "Editing Software", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Permanent Green Screen", "status", "No"));

	    model.addAttribute("schoolDetail",schoolDetail);
	    model.addAttribute("equipmentList", equipmentList);

	    return "SchoolManagement/districtManageStudioDetail";
	}
	
	@GetMapping("/declined/{id}")
	public String declinedStudio(@PathVariable int id, Model model) {
	    // Mock school details
	    Map<String, Object> schoolDetail = new HashMap<>();
	    schoolDetail.put("id", 1);
	    schoolDetail.put("schoolName", "SK Pagoh");
	    schoolDetail.put("district", "Pagoh");
	    schoolDetail.put("teacherInCharge", "Ms. Syafidah");
	    schoolDetail.put("studioLevel", 2);
	    schoolDetail.put("equipmentImages", "https://drive.google.com/your-link-here"); // Example

	    // List of equipment
	    List<Map<String, String>> equipmentList = new ArrayList<>();
	    equipmentList.add(Map.of("name", "TV Program/Show Corner", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Editing Corner", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Smartphone", "status", "No"));
	    equipmentList.add(Map.of("name", "External Mic", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Monopod", "status", "No"));
	    equipmentList.add(Map.of("name", "Mobile Green Screen Set", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Tripod", "status", "No"));
	    equipmentList.add(Map.of("name", "Wireless Mic", "status", "No"));
	    equipmentList.add(Map.of("name", "Mobile Lighting", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Webcam", "status", "No"));
	    equipmentList.add(Map.of("name", "Mobile Lighting (3 Point)", "status", "No"));
	    equipmentList.add(Map.of("name", "Camera", "status", "No"));
	    equipmentList.add(Map.of("name", "Editing Software", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Permanent Green Screen", "status", "No"));

	    model.addAttribute("schoolDetail",schoolDetail);
	    model.addAttribute("equipmentList", equipmentList);

	    return "SchoolManagement/districtManageStudioDetail";
	}
	
	@GetMapping("/stateDistrictsStudio")
    public String viewDistrictsStudio(Model model) {
		List<Map<String, Object>> districts = new ArrayList<>();

        //District 1
        Map<String, Object> district1 = new HashMap<>();
        district1.put("dID", 1);
        district1.put("districtName", "Batu Pahat");
        district1.put("personInCharge", "Mr. Zulaifiq");
        district1.put("totalSchools", "3");
        districts.add(district1);
        
        //District 2
        Map<String, Object> district2 = new HashMap<>();
        district2.put("dID", 2);
        district2.put("districtName", "Pagoh");
        district2.put("personInCharge", "Ms. Anita");
        district2.put("totalSchools", "5");
        districts.add(district2);
        
      //District 3
        Map<String, Object> district3 = new HashMap<>();
        district3.put("dID", 3);
        district3.put("districtName", "Kota Tinggi");
        district3.put("personInCharge", "Mr. Afiq");
        district3.put("totalSchools", "3");
        districts.add(district3);
        
        // Add the map to the model
        model.addAttribute("districts", districts);

        return "SchoolManagement/stateDistrictsStudio"; // The name of the HTML/Thymeleaf template
    }
	
	@GetMapping("/stateManageStudio/{dID}")
	public String viewDistrictSchoolsStudio(@PathVariable int dID, Model model) {
		List<Map<String, Object>> schoolsList = new ArrayList<>();

		// School 1
        Map<String, Object> school1 = new HashMap<>();
        school1.put("id", 1);
        school1.put("schoolName", "SK Pagoh");
        school1.put("teacherInCharge", "Ms. Syafidah");
        school1.put("schoolPhone", "07-5562577");
        school1.put("schoolAddress", "Sekolah Kebangsaan Pagoh, KM 33, Pekan Pagoh, 84600 Pagoh, Johor");
        school1.put("postcode", 84600);
        school1.put("district", "Pagoh");
        school1.put("tvpssVersion", "2");
        school1.put("studioLevel", "2");
        school1.put("schoolLogo", "path/to/logo.png");
        schoolsList.add(school1);
        
        // School 2
        Map<String, Object> school2 = new HashMap<>();
        school2.put("id", 2);
        school2.put("schoolName", "SK Batu Pahat");
        school2.put("teacherInCharge", "Ms. Afiqah");
        school2.put("schoolPhone", "07-5562577");
        school2.put("schoolAddress", "Sekolah Kebangsaan Batu Pahat, KM4, 83000 Batu Pahat, Johor ");
        school2.put("postcode", 83000);
        school2.put("district", "Batu Pahat");
        school2.put("tvpssVersion", "3");
        school2.put("studioLevel", "2");
        school2.put("schoolLogo", "path/to/logo.png");
        schoolsList.add(school2);

	    // Add the list of schools to the model
	    model.addAttribute("schoolsList", schoolsList);

	    return "SchoolManagement/stateManageStudio";
	}
	
	@GetMapping("/stateManageStudioDetail/{id}")
	public String viewStudioDetail(@PathVariable int id, Model model) {
	    // Mock school details
	    Map<String, Object> schoolDetail = new HashMap<>();
	    schoolDetail.put("id", id);
	    schoolDetail.put("schoolName", "SK Pagoh");
	    schoolDetail.put("district", "Pagoh");
	    schoolDetail.put("teacherInCharge", "Ms. Syafidah");
	    schoolDetail.put("studioLevel", 2);
	    schoolDetail.put("equipmentImages", "https://drive.google.com/your-link-here"); // Example

	    // List of equipment
	    List<Map<String, String>> equipmentList = new ArrayList<>();
	    equipmentList.add(Map.of("name", "TV Program/Show Corner", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Editing Corner", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Smartphone", "status", "No"));
	    equipmentList.add(Map.of("name", "External Mic", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Monopod", "status", "No"));
	    equipmentList.add(Map.of("name", "Mobile Green Screen Set", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Tripod", "status", "No"));
	    equipmentList.add(Map.of("name", "Wireless Mic", "status", "No"));
	    equipmentList.add(Map.of("name", "Mobile Lighting", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Webcam", "status", "No"));
	    equipmentList.add(Map.of("name", "Mobile Lighting (3 Point)", "status", "No"));
	    equipmentList.add(Map.of("name", "Camera", "status", "No"));
	    equipmentList.add(Map.of("name", "Editing Software", "status", "Yes"));
	    equipmentList.add(Map.of("name", "Permanent Green Screen", "status", "No"));

	    model.addAttribute("schoolDetail",schoolDetail);
	    model.addAttribute("equipmentList", equipmentList);

	    return "SchoolManagement/stateManageStudioDetail";
	}

}
