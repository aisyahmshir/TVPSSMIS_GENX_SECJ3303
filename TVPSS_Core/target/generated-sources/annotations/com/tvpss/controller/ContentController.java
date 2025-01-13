//package com.tvpss.controller;
//
//import com.tvpss.model.ContentModel;
//import com.tvpss.model.CrewModel;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Controller
//public class ContentController {
//	
//	private List<ContentModel> contentList = new ArrayList<>();
//	
//	@GetMapping("/StateContent")
//	public String homeState(Model model) {
//	    System.out.println("I'm in");
//
//	    // List to store district data, where each district contains schools
//	    List<Map<String, Object>> districtsData = new ArrayList<>();
//
//	    // Sample data for 5 districts and their schools
//	    for (int i = 1; i <= 5; i++) {
//	        Map<String, Object> district = new HashMap<>();
//	        district.put("districtName", "District " + i);
//
//	        // List to store schools for the current district
//	        List<Map<String, Object>> schools = new ArrayList<>();
//
//	        // Sample data for 3 schools in each district
//	        for (int j = 1; j <= 3; j++) {
//	            Map<String, Object> school = new HashMap<>();
//	            school.put("id", i * 10 + j); // Add an ID for each school
//	            school.put("name", "School " + (i * 10 + j));
//	            school.put("address", "District " + i);
//	            school.put("version", "Version " + (i * 10 + j));
//
//	            // Sample contents for the current school
//	            List<Map<String, Object>> contents = new ArrayList<>();
//	            for (int k = 1; k <= 2; k++) { // Each school has 2 contents
//	                Map<String, Object> content = new HashMap<>();
//	                content.put("title", "Event " + k);
//	                content.put("date", "2024-11-" + (10 + k)); // Example dates
//	                content.put("url", "https://example.com/event" + k);
//	                content.put("eventName", "Event Name " + k);
//	                content.put("details", "Details for event " + k);
//	                contents.add(content);
//	            }
//	            school.put("contents", contents);
//
//	            // Add the school to the list of schools for the district
//	            schools.add(school);
//	        }
//
//	        // Add the list of schools to the district data
//	        district.put("schools", schools);
//
//	        // Add the district to the list of districts data
//	        districtsData.add(district);
//	    }
//
//	    //System.out.println(districtsData);
//	    // Add the districts data to the model for rendering
//	    model.addAttribute("districtsData", districtsData);
//	    //model.addAttribute("districts", districtsData.stream().map(d -> d.get("districtName")).collect(Collectors.toList()));
//
//	    return "ContentLibrary/StateContent"; // Path to the HTML template
//	}
//	
//	@GetMapping("/StateContentViewMore/{id}")
//	public String viewMoreState(@PathVariable("id") int schoolId, Model model) {
//	    // Find the school by ID from the districtsData (hardcoded)
//		List<Map<String, Object>> districtsData = new ArrayList<>();
//
//	    // Sample data for 5 districts and their schools
//	    for (int i = 1; i <= 5; i++) {
//	        Map<String, Object> district = new HashMap<>();
//	        district.put("districtName", "District " + i);
//
//	        // List to store schools for the current district
//	        List<Map<String, Object>> schools = new ArrayList<>();
//
//	        // Sample data for 3 schools in each district
//	        for (int j = 1; j <= 3; j++) {
//	            Map<String, Object> school = new HashMap<>();
//	            school.put("id", i * 10 + j); // Add an ID for each school
//	            school.put("name", "School " + (i * 10 + j));
//	            school.put("address", "District " + i);
//	            school.put("version", "Version " + (i * 10 + j));
//
//	            // Sample contents for the current school
//	            List<Map<String, Object>> contents = new ArrayList<>();
//	            for (int k = 1; k <= 2; k++) { // Each school has 2 contents
//	                Map<String, Object> content = new HashMap<>();
//	                content.put("title", "Event " + k);
//	                content.put("dateCreated", "2024-11-" + (10 + k)); // Example dates
//	                content.put("youtubeUrl", "https://example.com/event" + k);
//	                content.put("eventName", "Event Name " + k);
//	                content.put("details", "Details for event " + k);
//	                contents.add(content);
//	            }
//	            school.put("contents", contents);
//
//	            // Add the school to the list of schools for the district
//	            schools.add(school);
//	        }
//
//	        // Add the list of schools to the district data
//	        district.put("schools", schools);
//
//	        // Add the district to the list of districts data
//	        districtsData.add(district);
//	    }
//
//	    //System.out.println(districtsData);
//	    // Add the districts data to the model for rendering
//	    model.addAttribute("districtsData", districtsData);
//	    
//
//	    Map<String, Object> schoolData = null;
//	    for (Map<String, Object> district : districtsData) {
//	        List<Map<String, Object>> schools = (List<Map<String, Object>>) district.get("schools");
//	        for (Map<String, Object> school : schools) {
//	            if ((int) school.get("id") == schoolId) {
//	                schoolData = school;
//	                break;
//	            }
//	        }
//	        if (schoolData != null) {
//	            break;
//	        }
//	    }
//
//	    // If schoolData is found, add it to the model
//	    if (schoolData != null) {
//	        model.addAttribute("schoolData", schoolData);
//	        return "ContentLibrary/StateContentViewMore"; // Return the detail page
//	    } else {
//	        return "error"; // Return error page if school is not found
//	    }
//	}
//	
//	@GetMapping("/DistrictContent")
//	public String homeDistrict(Model model) {
//	    System.out.println("I'm in");
//	    
//	    List<Map<String, Object>> schools = new ArrayList<>();
//
//        // Data for 5 schools
//        for (int i = 1; i <= 5; i++) {
//            Map<String, Object> school = new HashMap<>();
//            school.put("id", i);
//            school.put("name", "School " + i);
//            school.put("address", "Address " + i);
//            school.put("version", "Version " + i);
//
//            List<Map<String, Object>> contents = new ArrayList<>();
//            for (int k = 1; k <= 2; k++) { // Each school has 2 contents
//                Map<String, Object> content = new HashMap<>();
//                content.put("title", "Event " + k);
//                content.put("dateCreated", "2024-11-" + (10 + k)); // Example dates
//                content.put("youtubeUrl", "https://example.com/event" + k);
//                content.put("eventName", "Event Name " + k);
//                content.put("details", "Details for event " + k);
//                contents.add(content);
//            }
//            school.put("contents", contents);
//
//            // Add the school to the list
//            schools.add(school);
//        }
//
//        // Add the list of schools to the model
//        model.addAttribute("schools", schools);
//
//	    return "ContentLibrary/DistrictContent"; // Path to the HTML template
//	}
//	
//	
//	
//	@GetMapping("/DistrictContentViewMore/{id}")
//	public String viewMoreDistrict(@PathVariable("id") int schoolId, Model model) {
//		
//		List<Map<String, Object>> schools = new ArrayList<>();
//
//        // Data for 5 schools
//        for (int i = 1; i <= 5; i++) {
//            Map<String, Object> school = new HashMap<>();
//            school.put("id", i);
//            school.put("name", "School " + i);
//            school.put("address", "Address " + i);
//            school.put("version", "Version " + i);
//
//            List<Map<String, Object>> contents = new ArrayList<>();
//            for (int k = 1; k <= 2; k++) { // Each school has 2 contents
//                Map<String, Object> content = new HashMap<>();
//                content.put("title", "Event " + k);
//                content.put("dateCreated", "2024-11-" + (10 + k)); // Example dates
//                content.put("youtubeUrl", "https://example.com/event" + k);
//                content.put("eventName", "Event Name " + k);
//                content.put("details", "Details for event " + k);
//                contents.add(content);
//            }
//            school.put("contents", contents);
//
//            // Add the school to the list
//            schools.add(school);
//        }	    
//
//	    Map<String, Object> schoolData = null;
//	        for (Map<String, Object> school : schools) {
//	            if ((int) school.get("id") == schoolId) {
//	                schoolData = school;
//	                break;
//	            }
//	            
//	            if (schoolData != null) {
//		            break;
//		        }    
//	        }
//	        
//
//	    // If schoolData is found, add it to the model
//	    if (schoolData != null) {
//	        model.addAttribute("schoolData", schoolData);
//	        return "ContentLibrary/DistrictContentViewMore"; // Return the detail page
//	    } else {
//	        return "error"; // Return error page if school is not found
//	    }
//	}
//	
//
//	
//	@GetMapping("/SchoolContent")
//	public String homeSchool(Model model) {
//	    System.out.println("I'm in");
//
//	    // Example student details
//	    String role = "teacher"; // This could come from a user service or session
//	    
//	    // Add role to the model
//	    model.addAttribute("role", role);
//	    
//	    //List<ContentModel> contentList = new ArrayList<>();
////	    contentList.add(new ContentModel("Content 1", "2024-11-01", "https://youtube.com/video1", "Event 1", "Details about Content 1."));
////        contentList.add(new ContentModel("Content 2", "2024-11-15", "https://youtube.com/video2", "Event 2", "Details about Content 2."));
//	    
//	    List<Map<String, Object>> contents = new ArrayList<>();
//	    Map<String, Object> content1 = new HashMap<>();
//	    content1.put("title", "content title");
//	    content1.put("dateCreated", "some date");
//	    content1.put("youtubeUrl", "some url");
//	    content1.put("eventName", "name example");
//	    content1.put("details", "details example");
//
//	    Map<String, Object> content2 = new HashMap<>();
//	    content2.put("title", "Example School 2");
//	    content2.put("dateCreated", Arrays.asList("Alice Johnson", "Bob Williams", "Charlie Davis"));
//	    content2.put("youtubeUrl", "Ms. Alice");
//	    content2.put("eventName", "456 School Ave, City");
//	    content2.put("details", "Version 2.0");
//
//	    contents.add(content1);
//
//	    model.addAttribute("contents", contents);
//        
////        model.addAttribute("crewModel", contentList);
//
//	    // Return the view name
//	    return "ContentLibrary/SchoolContent";
//	}






//private List<ContentModel> contentList = new ArrayList<>();

//    public ContentController() {
//        // Initialize with some sample data
//        
//    }

// Fetch all content
//    @GetMapping("/fetchContentData")
//    @ResponseBody
//    public List<ContentModel> fetchContentData() {
//        return contentList;
//    }
//
//    // Add new content
//    @PostMapping("/addNewContent")
//    @ResponseBody
//    public Map<String, String> addNewContent(@RequestBody ContentModel newContent) {
//        contentList.add(newContent);
//
//        Map<String, String> response = new HashMap<>();
//        response.put("status", "success");
//        response.put("message", "New content added successfully!");
//        return response;
//    }
//
//    // Get specific content for "View More" modal
//    @GetMapping("/getContentDetails/{index}")
//    @ResponseBody
//    public ContentModel getContentDetails(@PathVariable int index) {
//        if (index >= 0 && index < contentList.size()) {
//            return contentList.get(index);
//        } else {
//            return null; // Alternatively, return a meaningful error response
//        }
//    }
//}

package com.tvpss.controller;

import com.tvpss.model.ContentModel;
import com.tvpss.model.School;

import com.tvpss.service.ContentService;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ContentController {

	private List<ContentModel> contentList = new ArrayList<>();
	
	@GetMapping("/StateContent")
	public String homeState(Model model, HttpSession session) {
	    System.out.println("Fetching state content");
	    String role = (String) session.getAttribute("role");
	    
	    if("STATE OFFICER".equalsIgnoreCase(role) == false) {
	    	return "UserManagement/login"; 
	    }
	    // List to store district data, where each district contains schools
	    List<Map<String, Object>> districtsData = new ArrayList<>();

	    // Step 1: Retrieve all district data directly from the database
	    List<Map<String, Object>> districts = ContentService.getAllDistricts(); // Returns a list of maps for district data

	    for (Map<String, Object> district : districts) {
	        // Create a map to store district data
	        Map<String, Object> districtData = new HashMap<>();
	        String districtName = (String) district.get("name");
	        int districtID = (int) district.get("districtID");

	        districtData.put("districtName", districtName);

	        // Step 2: Retrieve all schools in the current district
	        List<School> schoolsList = ContentService.getAllSchoolByDistrict(districtID);

	        // List to store schools for the current district
	        List<Map<String, Object>> schoolsData = new ArrayList<>();

	        for (School school : schoolsList) {
	            // Create a map to store school data
	            Map<String, Object> schoolData = new HashMap<>();
	            schoolData.put("schoolID", school.getSchoolID());
	            schoolData.put("name", school.getName());
	            schoolData.put("districtName", districtName); // Use district name as the school address
	            schoolData.put("tvpssVersion", school.getTvpssVersion()); // Assuming School has a getTvpssVersion() method

	            // Add the school data to the schools list
	            schoolsData.add(schoolData);
	        }

	        // Add the list of schools to the district data
	        districtData.put("schools", schoolsData);

	        // Add the district data to the list of districts data
	        districtsData.add(districtData);
	    }

	    // Add the districts data to the model for rendering
	    model.addAttribute("districtsData", districtsData);
        //String userID = (String) session.getAttribute("userID");

        // Add attributes to the model
        model.addAttribute("role", role);
        //model.addAttribute("userID", userID);

	    return "ContentLibrary/StateContent"; // Path to the HTML template
	}



//	@GetMapping("/StateContent")
//	public String homeState(Model model) {
//		System.out.println("I'm in");
//
//		// List to store district data, where each district contains schools
//		List<Map<String, Object>> districtsData = new ArrayList<>();
//
//		// Sample data for 5 districts and their schools
//		for (int i = 1; i <= 5; i++) {
//			Map<String, Object> district = new HashMap<>();
//			district.put("districtName", "District " + i);
//
//			// List to store schools for the current district
//			List<Map<String, Object>> schools = new ArrayList<>();
//
//			// Sample data for 3 schools in each district
//			for (int j = 1; j <= 3; j++) {
//				Map<String, Object> school = new HashMap<>();
//				school.put("id", i * 10 + j); // Add an ID for each school
//				school.put("name", "School " + (i * 10 + j));
//				school.put("address", "District " + i);
//				school.put("version", "Version " + (i * 10 + j));
//
//				// Sample contents for the current school
//				List<Map<String, Object>> contents = new ArrayList<>();
//				for (int k = 1; k <= 2; k++) { // Each school has 2 contents
//					Map<String, Object> content = new HashMap<>();
//					content.put("contentID", i * 100 + j * 10 + k); // Unique ID for content
//					content.put("title", "Event " + k);
//					content.put("date", "2024-11-" + (10 + k)); // Example dates
//					content.put("videoURL", "https://example.com/event" + k);
//					content.put("eventName", "Event Name " + k);
//					content.put("details", "Details for event " + k);
//					content.put("createdAt", "2024-11-" + (10 + k) + " 12:00:00");
//					content.put("schoolID", i * 10 + j); // Link content to the school
//
//					contents.add(content);
//				}
//				school.put("contents", contents);
//
//				// Add the school to the list of schools for the district
//				schools.add(school);
//			}
//
//			// Add the list of schools to the district data
//			district.put("schools", schools);
//
//			// Add the district to the list of districts data
//			districtsData.add(district);
//		}
//
//		// Add the districts data to the model for rendering
//		model.addAttribute("districtsData", districtsData);
//
//		return "ContentLibrary/StateContent"; // Path to the HTML template
//	}

	@GetMapping("/StateContentViewMore/{id}")
	public String viewMoreState(@PathVariable("id") int schoolID, Model model, HttpSession session) {
	    String role = (String) session.getAttribute("role");
	    
	    if("STATE OFFICER".equalsIgnoreCase(role) == false) {
	    	return "UserManagement/login"; 
	    }
		List<ContentModel> contentList = ContentService.getAllContentBySchoolID(schoolID);
	    
	    List<Map<String, Object>> contents = new ArrayList<>();
	    
	    for (ContentModel content : contentList) {
	        Map<String, Object> contentMap = new HashMap<>();
	        contentMap.put("contentID", content.getContentID()); // Unique ID for content
	        //System.out.print(content.getContentID());
	        contentMap.put("title", content.getTitle());
	        contentMap.put("date", content.getDate());
	        contentMap.put("videoURL", content.getVideoURL());
	        contentMap.put("eventName", content.getEventName());
	        contentMap.put("details", content.getDetails());
	        contentMap.put("createdAt", content.getCreatedAt());
	        contentMap.put("schoolID", content.getSchoolID());
	        contents.add(contentMap);
	    }

	    // Add the school and its contents to the model
	    //model.addAttribute("schoolID", schoolID); // School ID for context
	    model.addAttribute("contents", contents); // List of contents
		// Find the school by ID from the districtsData (hardcoded)
//		List<Map<String, Object>> districtsData = new ArrayList<>();
//
//		// Sample data for 5 districts and their schools
//		for (int i = 1; i <= 5; i++) {
//			Map<String, Object> district = new HashMap<>();
//			district.put("districtName", "District " + i);
//
//			// List to store schools for the current district
//			List<Map<String, Object>> schools = new ArrayList<>();
//
//			// Sample data for 3 schools in each district
//			for (int j = 1; j <= 3; j++) {
//				Map<String, Object> school = new HashMap<>();
//				school.put("id", i * 10 + j); // Add an ID for each school
//				school.put("name", "School " + (i * 10 + j));
//				school.put("address", "District " + i);
//				school.put("version", "Version " + (i * 10 + j));
//
//				// Sample contents for the current school
//				List<Map<String, Object>> contents = new ArrayList<>();
//				for (int k = 1; k <= 2; k++) { // Each school has 2 contents
//					Map<String, Object> content = new HashMap<>();
//					content.put("contentID", i * 100 + j * 10 + k); // Unique ID for content
//					content.put("title", "Event " + k);
//					content.put("date", "2024-11-" + (10 + k)); // Example dates
//					content.put("videoURL", "https://example.com/event" + k);
//					content.put("eventName", "Event Name " + k);
//					content.put("details", "Details for event " + k);
//					content.put("createdAt", "2024-11-" + (10 + k) + " 12:00:00");
//					content.put("schoolID", i * 10 + j); // Link content to the school
//
//					contents.add(content);
//				}
//				school.put("contents", contents);
//
//				// Add the school to the list of schools for the district
//				schools.add(school);
//			}
//
//			// Add the list of schools to the district data
//			district.put("schools", schools);
//
//			// Add the district to the list of districts data
//			districtsData.add(district);
//		}
//
//		Map<String, Object> schoolData = null;
//		for (Map<String, Object> district : districtsData) {
//			List<Map<String, Object>> schools = (List<Map<String, Object>>) district.get("schools");
//			for (Map<String, Object> school : schools) {
//				if ((int) school.get("id") == schoolId) {
//					schoolData = school;
//					break;
//				}
//			}
//			if (schoolData != null) {
//				break;
//			}
//		}
//
//		// If schoolData is found, add it to the model
//		if (schoolData != null) {
//			model.addAttribute("schoolData", schoolData);
//			return "ContentLibrary/StateContentViewMore"; // Return the detail page
//		} else {
//			return "error"; // Return error page if school is not found
//		}
	    return "ContentLibrary/StateContentViewMore";
	}

	@GetMapping("/DistrictContent")
	public String homeDistrict(Model model, HttpSession session) {
	    String role = (String) session.getAttribute("role");
	    
	    if("DISTRICT OFFICER".equalsIgnoreCase(role) == false) {
	    	return "UserManagement/login"; 
	    }
		// Fetch dynamic school data from CrewService
	    int districtID = (Integer) session.getAttribute("districtID");
	    System.out.println("Fetching district content");
	    System.out.println(districtID);

	    // Step 1: Get the list of schools by districtID
	    List<School> schoolList = ContentService.getAllSchoolByDistrict(districtID); // Call your ContentService method here

	    // Step 2: Prepare the school data
	    List<Map<String, Object>> schools = new ArrayList<>();

	    for (School school : schoolList) {
	        // Create a map to store school data
	        Map<String, Object> schoolMap = new HashMap<>();
	        schoolMap.put("schoolID", school.getSchoolID());
	        schoolMap.put("name", school.getName()); // Assuming School has a getName() method

	        // Step 3: Fetch the district name using the districtID
	        String districtName = ContentService.getDistrictNameByID(school.getDistrictID()); // Call your ContentService method here
//	        System.out.print(school.getSchoolID());
//	        System.out.print(school.getDistrictID());
	        schoolMap.put("districtName", districtName);
	        schoolMap.put("tvpssVersion", school.getTvpssVersion()); 
	        
	        //System.out.print(schoolMap);

	        // Add the school map to the list of schools
	        schools.add(schoolMap);
	    }

	    // Step 4: Add the list of schools to the model
	    model.addAttribute("schools", schools);
        //String userID = (String) session.getAttribute("id");

        // Add attributes to the model
        model.addAttribute("role", role);
        //model.addAttribute("userID", userID);

	    // Return the view name to render the district content
	    return "ContentLibrary/DistrictContent";
	}

//	public String homeDistrict(Model model) {
//		System.out.println("I'm in");
//
//		List<Map<String, Object>> schools = new ArrayList<>();
//
//		// Data for 5 schools
//		for (int i = 1; i <= 5; i++) {
//			Map<String, Object> school = new HashMap<>();
//			school.put("id", i);
//			school.put("name", "School " + i);
//			school.put("address", "Address " + i);
//			school.put("version", "Version " + i);
//
//			List<Map<String, Object>> contents = new ArrayList<>();
//			for (int k = 1; k <= 2; k++) { // Each school has 2 contents
//				Map<String, Object> content = new HashMap<>();
//				content.put("contentID", i * 100 + k); // Unique ID for content
//				content.put("title", "Event " + k);
//				content.put("date", "2024-11-" + (10 + k)); // Example dates
//				content.put("videoURL", "https://example.com/event" + k);
//				content.put("eventName", "Event Name " + k);
//				content.put("details", "Details for event " + k);
//				content.put("createdAt", "2024-11-" + (10 + k) + " 12:00:00");
//				content.put("schoolID", i); // Link content to the school
//
//				contents.add(content);
//			}
//			school.put("contents", contents);
//
//			// Add the school to the list
//			schools.add(school);
//		}
//
//		// Add the list of schools to the model
//		model.addAttribute("schools", schools);
//
//		return "ContentLibrary/DistrictContent"; // Path to the HTML template
//	}

	@GetMapping("/DistrictContentViewMore/{id}")
	public String viewMoreDistrict(@PathVariable("id") int schoolID, Model model,HttpSession session) {
		String role = (String) session.getAttribute("role");
	    
	    if("DISTRICT OFFICER".equalsIgnoreCase(role) == false) {
	    	return "UserManagement/login"; 
	    }
	    // Fetch the list of contents for the given schoolID
	    List<ContentModel> contentList = ContentService.getAllContentBySchoolID(schoolID);
	    
	    
	    List<Map<String, Object>> contents = new ArrayList<>();
	    
	    for (ContentModel content : contentList) {
	        Map<String, Object> contentMap = new HashMap<>();
	        contentMap.put("contentID", content.getContentID()); // Unique ID for content
	        //System.out.print(content.getContentID());
	        contentMap.put("title", content.getTitle());
	        contentMap.put("date", content.getDate());
	        contentMap.put("videoURL", content.getVideoURL());
	        contentMap.put("eventName", content.getEventName());
	        contentMap.put("details", content.getDetails());
	        contentMap.put("createdAt", content.getCreatedAt());
	        contentMap.put("schoolID", content.getSchoolID());
	        contents.add(contentMap);
	    }
        //String userID = (String) session.getAttribute("id");

        // Add attributes to the model
        model.addAttribute("role", role);
        //model.addAttribute("userID", userID);

	    // Add the school and its contents to the model
	    //model.addAttribute("schoolID", schoolID); // School ID for context
	    model.addAttribute("contents", contents); // List of contents

	    return "ContentLibrary/DistrictContentViewMore"; // Return the detail page
	}


	@GetMapping("/SchoolContent")
	public String homeSchool(Model model, HttpSession session) {
	    System.out.println("I'm in");
	    

		String role = (String) session.getAttribute("role");
	    
	    if("TEACHER".equalsIgnoreCase(role) == false) {
	    	return "UserManagement/login"; 
	    }

	    // Add role to the model
	    model.addAttribute("role", role);

        // Fetch dynamic school data from CrewService
	    int id = (Integer) session.getAttribute("id");
	    
	    List<ContentModel> contentList = ContentService.getAllContentBySchool(id);

	    // Creating the content for the page
	    List<Map<String, Object>> contents = new ArrayList<>();
	    
	 // Dynamically add all contents from contentList
	    for (ContentModel content : contentList) {
	        Map<String, Object> contentMap = new HashMap<>();
	        contentMap.put("contentID", content.getContentID()); // Unique ID for content
	        contentMap.put("title", content.getTitle());
	        contentMap.put("date", content.getDate());
	        contentMap.put("videoURL", content.getVideoURL());
	        contentMap.put("eventName", content.getEventName());
	        contentMap.put("details", content.getDetails());
	        contentMap.put("createdAt", content.getCreatedAt());
	        contentMap.put("schoolID", content.getSchoolID());
	        contents.add(contentMap);
	    }

	    // First content example (updated to match ContentModel attributes)
	    //Map<String, Object> content1 = new HashMap<>();
//	    content1.put("contentTitle", "content title");   // Renamed to contentTitle
//	    content1.put("dateCreated", "some date");        // Renamed to dateCreated
//	    content1.put("videoURL", "some url");            // Renamed to videoURL
//	    content1.put("eventName", "name example");       // Renamed to eventName
//	    content1.put("details", "details example");
	    
//	    content1.put("contentID", contentList.getContentID()); // Unique ID for content
//		content1.put("title", contentList.getTitle());
//		content1.put("date", contentList.getDate()); // Example dates
//		content1.put("videoURL", contentList.getVideoURL());
//		content1.put("eventName", contentList.getEventName());
//		content1.put("details", contentList.getDetails());
//		content1.put("createdAt", contentList.getCreatedAt());
//		content1.put("schoolID", contentList.getSchoolID());
//
//	    // Second content example (updated to match ContentModel attributes)
//	    Map<String, Object> content2 = new HashMap<>();
//	    content2.put("contentTitle", "Example School 2");   // Renamed to contentTitle
//	    content2.put("dateCreated", Arrays.asList("Alice Johnson", "Bob Williams", "Charlie Davis")); // Renamed to dateCreated
//	    content2.put("videoURL", "Ms. Alice");               // Renamed to videoURL
//	    content2.put("eventName", "456 School Ave, City");   // Renamed to eventName
//	    content2.put("details", "Version 2.0");
//
//	    // Adding the content to the list
//	    contents.add(content1);
//	    contents.add(content2);

	    // Adding the content list to the model
	    model.addAttribute("contents", contents);
	    role = (String) session.getAttribute("role");

        // Add attributes to the model
        model.addAttribute("role", role);
        model.addAttribute("userID", id);

	    // Return the view name to display the content
	    return "ContentLibrary/SchoolContent";
	}
	
	@PostMapping("/addContent")
	public String addContent(@RequestParam("title") String title,
	                         @RequestParam("date") String date,
	                         @RequestParam("videoURL") String videoURL,
	                         @RequestParam("eventName") String eventName,
	                         @RequestParam("details") String details,
	                         HttpSession session,
	                         Model model) {
	    // Create a new instance of ContentModel
	    ContentModel newContent = new ContentModel();
	    newContent.setTitle(title);
	    newContent.setDate(date);
	    newContent.setVideoURL(videoURL);
	    newContent.setEventName(eventName);
	    newContent.setDetails(details);
	    
	    // Fetch dynamic school data from CrewService
	     int id = (Integer) session.getAttribute("id");
	    System.out.println("Content Title: " + newContent.getTitle());
	    System.out.println("Content Date: " + newContent.getDate());
	    System.out.println("Content Video URL: " + newContent.getVideoURL());
	    System.out.println("Content Event Name: " + newContent.getEventName());
	    System.out.println("Content Details: " + newContent.getDetails());
	    //System.out.println("Content School ID: " + newContent.getSchoolID());

	    // Save the new content to the content_info table using the ContentService 
	    // (the schoolID attribute in the table should be derived using the id taken from session, and finding out which schoolID is linked to that id in the user table in the same database called tvpssdb)
	    ContentService.addContent(newContent, id);

	    // Fetch updated list of contents and add it to the model
	    //List<ContentModel> contents = ContentService.getAllContentBySchool();
	    List<ContentModel> contents = ContentService.getAllContentBySchool(id);
	    model.addAttribute("contents", contents);

	    //return "ContentLibrary/SchoolContent"; // Return back to the page after adding
	    return "redirect:/SchoolContent";
	}
	
	@PostMapping("/editContent")
	public String editContent(@RequestParam("contentID") int contentID,
	                          @RequestParam("title") String title,
	                          @RequestParam("date") String date,
	                          @RequestParam("eventName") String eventName,
	                          @RequestParam("details") String details,
	                          @RequestParam("videoURL") String videoURL) {
	    // Fetch the content from the database
	    ContentModel content = ContentService.findById(contentID);

	    // Update the content with the new values
	    content.setTitle(title);
	    content.setDate(date);
	    content.setEventName(eventName);
	    content.setDetails(details);
	    content.setVideoURL(videoURL);

	    // Save the updated content
	    ContentService.save(content);

	    // Redirect back to the content library page
	    return "redirect:/SchoolContent";
	}

}


