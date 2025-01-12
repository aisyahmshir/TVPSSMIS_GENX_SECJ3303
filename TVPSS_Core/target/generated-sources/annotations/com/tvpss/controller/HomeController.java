package com.tvpss.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

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
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tvpss.model.School;
import com.tvpss.model.UserModel;
import com.tvpss.service.SchoolService;
import com.tvpss.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private SchoolService schoolService;

    @GetMapping("UserManagement/home")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to Thymeleaf with Spring MVC!");
        return "UserManagement/home"; // This expects /WEB-INF/views/home.html
    }

    // Display the registration form
    @GetMapping("UserManagement/register")
    public String showRegisterForm(Model model) {
        return "UserManagement/register"; // Path to your register.html
    }

    @PostMapping("UserManagement/register")
    public String registerUser(@RequestParam("userid") String userID,
    						   @RequestParam("username") String name,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,      
                               @RequestParam("contact") String contactNo,
                               @RequestParam("role") String role,
                               @RequestParam("school") String school,
                              @RequestParam(value = "status", defaultValue = "Pending") String status,
                              Model model,
                              RedirectAttributes redirectAttributes) {

    	 // Log the registration attempt with the received data
        logger.info("Registration attempt for user: {}", name);
        
        // Step 1: Check if the email or contactNo already exists in the database
        if (userService.isEmailExist(email)) {
        	logger.warn("Email already exists: {}", email);
            model.addAttribute("errorMessage", "Email already registered.");
            return "UserManagement/register";
        }

        if (userService.isContactNoExist(contactNo)) {
        	logger.warn("Contact number already exists: {}", contactNo);
            model.addAttribute("errorMessage", "Contact number already registered.");
            return "UserManagement/register";
        }

        // Step 2: Find the school and district information based on the provided school name
        School schoolEntity = schoolService.getSchoolByName(school);

        if (schoolEntity == null) {
        	logger.warn("School not found: {}", school);
            model.addAttribute("errorMessage", "School not found.");
            return "UserManagement/register";
        }

        long schoolID = schoolEntity.getSchoolID();
        long districtID = schoolEntity.getDistrictID();
        
        logger.info("School found: {} (ID: {}) in District (ID: {})", schoolEntity.getName(), schoolID, districtID);
        
        // Step 3: Register the user with the appropriate schoolID and districtID
        boolean isRegistered = userService.registerUser(userID, name, contactNo, email, password, role, status, schoolID, districtID);

        // Step 4: Handle the registration result
        if (isRegistered) {
        	redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please wait for the admin to approve your account.");
            return "redirect:/UserManagement/login";  // Redirect to login page after successful registration
        } else {
            model.addAttribute("errorMessage", "Registration failed. Please try again.");
            return "/UserManagement/register";
        }
    }

    
//    @Autowired
//    private UserService userService;

    @GetMapping("UserManagement/login")
    public String showLoginForm() {
        return "UserManagement/login"; // Path to your login.html
    }
    
    @PostMapping("UserManagement/login")
    public String loginUser(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Validate user credentials and status
        //UserModel validationResponse = userService.validateLogin(email, password);
    	
        // Compare entered password with hashed password
        //boolean passwordMatch = bcryptEncoder.matches(password, user.getPassword());
    	UserModel user = userService.validateLogin(email, password);

    	if (user != null) {
            // Login successful, set user details in session
    		session.setAttribute("id", user.getId());
            session.setAttribute("userID", user.getUserID());
            session.setAttribute("role", user.getRole());
            session.setAttribute("status", user.getStatus());
            System.out.print(user.getDistrictID());
            session.setAttribute("districtID", user.getDistrictID());
            session.setAttribute("schoolID", user.getSchoolID());
            
            System.out.println("USERID in Controller: " + user.getUserID());

            String role = user.getRole();
            if ("ADMIN".equalsIgnoreCase(role)) {
                redirectAttributes.addFlashAttribute("successMessage", "Welcome Admin!");
                System.out.println("Role in Controller: " + role);

                return "redirect:/AdminDashboard"; // Redirect to Admin dashboard
            } 
            else if ("STUDENT".equalsIgnoreCase(role)) {
            	System.out.println("sup: " + user.getUserID());
                return "redirect:/studentMainView"; // Redirect to User dashboard
            } 
            else if ("TEACHER".equalsIgnoreCase(role)) {
                redirectAttributes.addFlashAttribute("successMessage", "Welcome Teacher!");
                return "redirect:/teacherSchoolView"; // Redirect to User dashboard
            } 
            else if ("DISTRICT OFFICER".equalsIgnoreCase(role)){

            	redirectAttributes.addFlashAttribute("successMessage", "Welcome District Officer!");
                return "redirect:/DistrictDashboard"; // Return to login page if role is invalid
            }
            else if ("STATE OFFICER".equalsIgnoreCase(role)){
            	redirectAttributes.addFlashAttribute("successMessage", "Welcome State Officer!");
                return "redirect:/StateDashboard"; // Return to login page if role is invalid
            } 
            else {
            // Login failed
            	model.addAttribute("errorMessage", "Role not recognized. Contact the administrator.");
                return "UserManagement/login"; 
            }
    	}
    	
    	model.addAttribute("errorMessage", "Invalid email or password, or account not approved.");
        return "UserManagement/login"; // Return to the login page
    }
   
 // Method to display the Manage Profile page with all users
    @GetMapping("UserManagement/manageProfile")
    public String manageProfile(Model model, HttpSession session) {
    	
		String loggedInUserId = (String) session.getAttribute("userID");
		        
		        if (loggedInUserId == null) {
		            model.addAttribute("error", "User is not logged in");
		            return "errorPage";
		        }
        // Fetch all users from the database using the UserService
        List<UserModel> users = userService.getApprovedUsers();
        
        // Add the list of users to the model
        model.addAttribute("users", users);
        
        // Return the view path for the manageProfile.html
        return "UserManagement/manageProfile";
    }

    // Method to handle user deletion
    @PostMapping("UserManagement/deleteUser")
    public String deleteUser(@RequestParam("userID") String userID, Model model) {
        // Call the delete method from UserService to delete the user by userId
        boolean isDeleted = userService.deleteUser(userID);

        // Display a message based on whether the user was deleted successfully
        if (isDeleted) {
            model.addAttribute("message", "User deleted successfully");
        } else {
            model.addAttribute("message", "Failed to delete user");
        }

        // Fetch the updated user list and pass it to the view
        List<UserModel> users = userService.getAllUsers();
        model.addAttribute("users", users);
        
        return "UserManagement/manageProfile";  // Reload the manageProfile page
    }


 
    @GetMapping("UserManagement/approveUsers")
    public String viewPendingUsers(Model model, HttpSession session) {
    	
		String loggedInUserId = (String) session.getAttribute("userID");
		        
		        if (loggedInUserId == null) {
		            model.addAttribute("error", "User is not logged in");
		            return "errorPage";
		        }
        List<UserModel> pendingUsers = userService.getPendingUsers();
        model.addAttribute("pendingUsers", pendingUsers);
        return "UserManagement/approveUsers"; // Path to your pendingUsers.html
    }
    
    @PostMapping("/UserManagement/approve/{userID}")
    public String approveUser(@PathVariable("userID") String userID, RedirectAttributes redirectAttributes) {
        boolean success = userService.updateUserStatus(userID, "Approved");
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "User approved successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to approve user.");
        }
        return "redirect:/UserManagement/approveUsers";
    }

    @PostMapping("/UserManagement/delete/{userID}")
    public String deleteUser(@PathVariable("userID") String userID, RedirectAttributes redirectAttributes) {
        boolean success = userService.deleteUser(userID);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete user.");
        }
        return "redirect:/UserManagement/approveUsers";
    }
  
    
    @RequestMapping("/UserManagement/editProfile")
    public String showEditProfileForm(Model model, HttpSession session) {
        // Retrieve the userID directly from the session
        String loggedInUserId = (String) session.getAttribute("userID"); // Assuming the userID is stored in the session
        System.out.println("haha "+loggedInUserId);
        if (loggedInUserId == null) {
            model.addAttribute("error", "User is not logged in");
            return "errorAuth"; // Redirect to an error page or handle accordingly
        }

        // Fetch user details by ID
        UserModel user = userService.findByUserId(loggedInUserId); // Assuming userID is String
        
        if (user != null) {
            model.addAttribute("user", user); // Add user to the model
            return "UserManagement/editProfile"; // Return the edit profile view
        } else {
            model.addAttribute("error", "User not found");
            return "errorPage"; // Show error page if user not found
        }
    }
    
    @PostMapping("/UserManagement/editProfile/{userID}")
    public String updateProfile(@PathVariable("userID") String userID, 
                                @RequestParam("name") String name, 
                                @RequestParam("contactNo") String contactNo,
                                @RequestParam("email") String email, 
                                Model model, 
                                HttpSession session, RedirectAttributes redirectAttributes) {

        String loggedInUserId = (String) session.getAttribute("userID");
        
        if (loggedInUserId == null) {
            model.addAttribute("error", "User is not logged in");
            return "errorPage";
        }

        // Create a UserModel object with updated details
        UserModel user = new UserModel();
        user.setUserID(userID);
        user.setName(name);
        user.setContactNo(contactNo);
        user.setEmail(email);

        // Call the update method in UserService
        boolean isUpdated = userService.updateUserDetails(user);
        
        if (isUpdated) {
        	redirectAttributes.addFlashAttribute("success", "Details updated successfully.");
            return "redirect:/UserManagement/editProfile";
        } else {
        	redirectAttributes.addFlashAttribute("error", "Failed to update details.");
            return "UserManagement/editProfile/" + userID;
        }
    }
    
    @PostMapping("/UserManagement/changePassword")
    public String changePassword(@RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 @RequestParam("userID") String userID, // Get userID from the form
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {
        
        // Fetch the logged-in user's ID from the session
        String loggedInUserId = (String) session.getAttribute("userID");
        
        // Check if the user is logged in
        if (loggedInUserId == null) {
            redirectAttributes.addFlashAttribute("error", "User is not logged in");
            return "redirect:UserManagement/login"; // Redirect to login page if not logged in
        }

        // Ensure that the userID passed in the form matches the logged-in user
        if (!loggedInUserId.equals(userID)) {
            redirectAttributes.addFlashAttribute("error", "You cannot change the password for another user");
            return "redirect:/UserManagement/editProfile/" + loggedInUserId; // Redirect back to their profile if they try to change another user's password
        }

        // Check if the new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/UserManagement/editProfile/" + loggedInUserId; // Redirect back to the profile page with an error
        }

        // Optional: Validate password strength (e.g., length, special characters)
        // You can add custom password validation logic here

        // Call the service method to update the password
        boolean isPasswordChanged = userService.updatePassword(loggedInUserId, newPassword);
        if (isPasswordChanged) {
            redirectAttributes.addFlashAttribute("success", "Password updated successfully.");
        } else {
        	logger.debug("Failed to update password for user: {}", loggedInUserId);
            redirectAttributes.addFlashAttribute("error", "Failed to update the password. Please try again.");
        }

        // Redirect back to the profile page with a success or error message
        return "redirect:/UserManagement/editProfile/";
    }
    

    @RequestMapping("UserManagement/logout")
    public String logout() {
        // Manually clear the authentication context to log out the user
        SecurityContextHolder.clearContext();
        
        // Redirect to the login page after logout
        return "redirect:/UserManagement/login";
    }


        
}