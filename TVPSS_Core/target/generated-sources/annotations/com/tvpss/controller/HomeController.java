package com.tvpss.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("UserManagement/home")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to Thymeleaf with Spring MVC!");
        return "UserManagement/home"; // This expects /WEB-INF/views/home.html
    }
    @GetMapping("UserManagement/chart")
    public String getChartData(Model model) {
        // Example data to display on the chart
        model.addAttribute("labels", List.of("January", "February", "March", "April", "May"));
        model.addAttribute("data", List.of(65, 59, 80, 81, 56));

        return "UserManagement/chart";
    }
    
 // Display the registration form
    @GetMapping("UserManagement/register")
    public String showRegisterForm(Model model) {
        return "UserManagement/register"; // Path to your register.html
    }

    // Handle form submission
//    @PostMapping("UserManagement/register")
//    public String registerUser(User user, Model model) {
//        // Save the user details into the database
//        userRepository.save(user);
//
//        // Redirect or show a success message
//        return "redirect:/login"; // You can change this to a success page or another view
//    }
    
    @GetMapping("UserManagement/login")
    public String showLoginForm() {
        return "UserManagement/login"; // Path to your login.html
    }

    // Process Login Request
//    @PostMapping("UserManagement/login")
//    public String processLogin(
//            @RequestParam("email") String email,
//            @RequestParam("password") String password,
//            Model model) {
//        // Authenticate user using UserService
//        boolean isAuthenticated = userService.authenticateUser(email, password);
//
//        if (isAuthenticated) {
//            // Redirect to home page or dashboard
//            return "redirect:/UserManagement/home";
//        } else {
//            // Return to login with error message
//            model.addAttribute("error", "Invalid email or password");
//            return "UserManagement/login";
//        }
//    }

 // Method to display the Manage Profile page with sample data
    @GetMapping("UserManagement/manageProfile")
    public String manageProfile(Model model) {
//         Create a list of sample users
        List<User> users = new ArrayList<>();

        users.add(new User(1, "JohnDoe", "johndoe@example.com"));
        users.add(new User(2, "JaneSmith", "janesmith@example.com"));
        users.add(new User(3, "AliceJones", "alicejones@example.com"));
//
//        // Pass the sample data to the Thymeleaf view
        model.addAttribute("users", users);

        return "UserManagement/manageProfile"; // Path to your manageProfile.html
    }


    // Method to delete a user by ID
//    @PostMapping("UserManagement/manageProfile/delete/{id}")
//    public String deleteUser(@PathVariable("id") Long id) {
//        // Call the service method to delete the user
//        userService.deleteUserById(id);
//
//        // Redirect to the Manage Profile page
//        return "redirect:/UserManagement/manageProfile";
//    }
    @GetMapping("UserManagement/approveUsers")
    public String approveUsers(Model model) {
        // Create a sample list of users
        List<User> users = new ArrayList<>();
        users.add(new User(1, "JohnDoe", "john.doe@delima.com"));
        users.add(new User(2, "JaneSmith", "jane.smith@delima.com"));
        users.add(new User(3, "AliceWonder", "alice.wonder@delima.com"));

        // Add the list of users to the model
        model.addAttribute("users", users);

        // Return the view name for displaying the users
        return "UserManagement/approveUsers";  // Name of the Thymeleaf template
    }
    
    
    public static class User {
        private int id;
        private String username;
        private String delimaAccount;

        public User(int id, String username, String delimaAccount) {
            this.id = id;
            this.username = username;
            this.delimaAccount = delimaAccount;
        }

        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getDelimaAccount() {
            return delimaAccount;
        }

        public void setDelimaAccount(String delimaAccount) {
            this.delimaAccount = delimaAccount;
        }
    }


}