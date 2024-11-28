package com.tvpss.controller;

import com.tvpss.model.ContentModel;
import com.tvpss.model.ContentModel.Content;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ContentController {

    private List<Content> contentList = new ArrayList<>();

    public ContentController() {
        // Initialize with some sample data
        contentList.add(new Content("Content 1", "2024-11-01", "https://youtube.com/video1", "Event 1", "Details about Content 1."));
        contentList.add(new Content("Content 2", "2024-11-15", "https://youtube.com/video2", "Event 2", "Details about Content 2."));
    }

    // Fetch all content
    @GetMapping("/fetchContentData")
    @ResponseBody
    public List<Content> fetchContentData() {
        return contentList;
    }

    // Add new content
    @PostMapping("/addNewContent")
    @ResponseBody
    public Map<String, String> addNewContent(@RequestBody Content newContent) {
        contentList.add(newContent);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "New content added successfully!");
        return response;
    }

    // Get specific content for "View More" modal
    @GetMapping("/getContentDetails/{index}")
    @ResponseBody
    public Content getContentDetails(@PathVariable int index) {
        if (index >= 0 && index < contentList.size()) {
            return contentList.get(index);
        } else {
            return null; // Alternatively, return a meaningful error response
        }
    }
}
