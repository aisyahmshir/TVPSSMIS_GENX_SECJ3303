package com.tvpss.controller;

import com.tvpss.model.ContentModel;
import com.tvpss.model.CrewModel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ContentController {
	
	private List<ContentModel> contentList = new ArrayList<>();
	
	@GetMapping("/StateContent")
	public String homeState(Model model) {
	    System.out.println("I'm in");

	    // List to store district data, where each district contains schools
	    List<Map<String, Object>> districtsData = new ArrayList<>();

	    // Sample data for 5 districts and their schools
	    for (int i = 1; i <= 5; i++) {
	        Map<String, Object> district = new HashMap<>();
	        district.put("districtName", "District " + i);

	        // List to store schools for the current district
	        List<Map<String, Object>> schools = new ArrayList<>();

	        // Sample data for 3 schools in each district
	        for (int j = 1; j <= 3; j++) {
	            Map<String, Object> school = new HashMap<>();
	            school.put("id", i * 10 + j); // Add an ID for each school
	            school.put("name", "School " + (i * 10 + j));
	            school.put("address", "District " + i);
	            school.put("version", "Version " + (i * 10 + j));

	            // Sample contents for the current school
	            List<Map<String, Object>> contents = new ArrayList<>();
	            for (int k = 1; k <= 2; k++) { // Each school has 2 contents
	                Map<String, Object> content = new HashMap<>();
	                content.put("title", "Event " + k);
	                content.put("date", "2024-11-" + (10 + k)); // Example dates
	                content.put("url", "https://example.com/event" + k);
	                content.put("eventName", "Event Name " + k);
	                content.put("details", "Details for event " + k);
	                contents.add(content);
	            }
	            school.put("contents", contents);

	            // Add the school to the list of schools for the district
	            schools.add(school);
	        }

	        // Add the list of schools to the district data
	        district.put("schools", schools);

	        // Add the district to the list of districts data
	        districtsData.add(district);
	    }

	    //System.out.println(districtsData);
	    // Add the districts data to the model for rendering
	    model.addAttribute("districtsData", districtsData);
	    //model.addAttribute("districts", districtsData.stream().map(d -> d.get("districtName")).collect(Collectors.toList()));

	    return "ContentLibrary/StateContent"; // Path to the HTML template
	}
	
	@GetMapping("/StateContentViewMore/{id}")
	public String viewMoreState(@PathVariable("id") int schoolId, Model model) {
	    // Find the school by ID from the districtsData (hardcoded)
		List<Map<String, Object>> districtsData = new ArrayList<>();

	    // Sample data for 5 districts and their schools
	    for (int i = 1; i <= 5; i++) {
	        Map<String, Object> district = new HashMap<>();
	        district.put("districtName", "District " + i);

	        // List to store schools for the current district
	        List<Map<String, Object>> schools = new ArrayList<>();

	        // Sample data for 3 schools in each district
	        for (int j = 1; j <= 3; j++) {
	            Map<String, Object> school = new HashMap<>();
	            school.put("id", i * 10 + j); // Add an ID for each school
	            school.put("name", "School " + (i * 10 + j));
	            school.put("address", "District " + i);
	            school.put("version", "Version " + (i * 10 + j));

	            // Sample contents for the current school
	            List<Map<String, Object>> contents = new ArrayList<>();
	            for (int k = 1; k <= 2; k++) { // Each school has 2 contents
	                Map<String, Object> content = new HashMap<>();
	                content.put("title", "Event " + k);
	                content.put("dateCreated", "2024-11-" + (10 + k)); // Example dates
	                content.put("youtubeUrl", "https://example.com/event" + k);
	                content.put("eventName", "Event Name " + k);
	                content.put("details", "Details for event " + k);
	                contents.add(content);
	            }
	            school.put("contents", contents);

	            // Add the school to the list of schools for the district
	            schools.add(school);
	        }

	        // Add the list of schools to the district data
	        district.put("schools", schools);

	        // Add the district to the list of districts data
	        districtsData.add(district);
	    }

	    //System.out.println(districtsData);
	    // Add the districts data to the model for rendering
	    model.addAttribute("districtsData", districtsData);
	    

	    Map<String, Object> schoolData = null;
	    for (Map<String, Object> district : districtsData) {
	        List<Map<String, Object>> schools = (List<Map<String, Object>>) district.get("schools");
	        for (Map<String, Object> school : schools) {
	            if ((int) school.get("id") == schoolId) {
	                schoolData = school;
	                break;
	            }
	        }
	        if (schoolData != null) {
	            break;
	        }
	    }

	    // If schoolData is found, add it to the model
	    if (schoolData != null) {
	        model.addAttribute("schoolData", schoolData);
	        return "ContentLibrary/StateContentViewMore"; // Return the detail page
	    } else {
	        return "error"; // Return error page if school is not found
	    }
	}
	
	@GetMapping("/DistrictContent")
	public String homeDistrict(Model model) {
	    System.out.println("I'm in");
	    
	    List<Map<String, Object>> schools = new ArrayList<>();

        // Data for 5 schools
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> school = new HashMap<>();
            school.put("id", i);
            school.put("name", "School " + i);
            school.put("address", "Address " + i);
            school.put("version", "Version " + i);

            List<Map<String, Object>> contents = new ArrayList<>();
            for (int k = 1; k <= 2; k++) { // Each school has 2 contents
                Map<String, Object> content = new HashMap<>();
                content.put("title", "Event " + k);
                content.put("dateCreated", "2024-11-" + (10 + k)); // Example dates
                content.put("youtubeUrl", "https://example.com/event" + k);
                content.put("eventName", "Event Name " + k);
                content.put("details", "Details for event " + k);
                contents.add(content);
            }
            school.put("contents", contents);

            // Add the school to the list
            schools.add(school);
        }

        // Add the list of schools to the model
        model.addAttribute("schools", schools);

	    return "ContentLibrary/DistrictContent"; // Path to the HTML template
	}
	
	
	
	@GetMapping("/DistrictContentViewMore/{id}")
	public String viewMoreDistrict(@PathVariable("id") int schoolId, Model model) {
		
		List<Map<String, Object>> schools = new ArrayList<>();

        // Data for 5 schools
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> school = new HashMap<>();
            school.put("id", i);
            school.put("name", "School " + i);
            school.put("address", "Address " + i);
            school.put("version", "Version " + i);

            List<Map<String, Object>> contents = new ArrayList<>();
            for (int k = 1; k <= 2; k++) { // Each school has 2 contents
                Map<String, Object> content = new HashMap<>();
                content.put("title", "Event " + k);
                content.put("dateCreated", "2024-11-" + (10 + k)); // Example dates
                content.put("youtubeUrl", "https://example.com/event" + k);
                content.put("eventName", "Event Name " + k);
                content.put("details", "Details for event " + k);
                contents.add(content);
            }
            school.put("contents", contents);

            // Add the school to the list
            schools.add(school);
        }	    

	    Map<String, Object> schoolData = null;
	        for (Map<String, Object> school : schools) {
	            if ((int) school.get("id") == schoolId) {
	                schoolData = school;
	                break;
	            }
	            
	            if (schoolData != null) {
		            break;
		        }    
	        }
	        

	    // If schoolData is found, add it to the model
	    if (schoolData != null) {
	        model.addAttribute("schoolData", schoolData);
	        return "ContentLibrary/DistrictContentViewMore"; // Return the detail page
	    } else {
	        return "error"; // Return error page if school is not found
	    }
	}
	

	
	@GetMapping("/SchoolContent")
	public String homeSchool(Model model) {
	    System.out.println("I'm in");

	    // Example student details
	    String role = "teacher"; // This could come from a user service or session
	    
	    // Add role to the model
	    model.addAttribute("role", role);
	    
	    //List<ContentModel> contentList = new ArrayList<>();
//	    contentList.add(new ContentModel("Content 1", "2024-11-01", "https://youtube.com/video1", "Event 1", "Details about Content 1."));
//        contentList.add(new ContentModel("Content 2", "2024-11-15", "https://youtube.com/video2", "Event 2", "Details about Content 2."));
	    
	    List<Map<String, Object>> contents = new ArrayList<>();
	    Map<String, Object> content1 = new HashMap<>();
	    content1.put("title", "content title");
	    content1.put("dateCreated", "some date");
	    content1.put("youtubeUrl", "some url");
	    content1.put("eventName", "name example");
	    content1.put("details", "details example");

	    Map<String, Object> content2 = new HashMap<>();
	    content2.put("title", "Example School 2");
	    content2.put("dateCreated", Arrays.asList("Alice Johnson", "Bob Williams", "Charlie Davis"));
	    content2.put("youtubeUrl", "Ms. Alice");
	    content2.put("eventName", "456 School Ave, City");
	    content2.put("details", "Version 2.0");

	    contents.add(content1);

	    model.addAttribute("contents", contents);
        
//        model.addAttribute("crewModel", contentList);

	    // Return the view name
	    return "ContentLibrary/SchoolContent";
	}
	
	
	
	


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
}
