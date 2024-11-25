package com.tvpss.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to Thymeleaf with Spring MVC!");
        return "home"; // This expects /WEB-INF/views/home.html
    }
    @GetMapping("/chart")
    public String getChartData(Model model) {
        // Example data to display on the chart
        model.addAttribute("labels", List.of("January", "February", "March", "April", "May"));
        model.addAttribute("data", List.of(65, 59, 80, 81, 56));

        return "chart";
    }

}
