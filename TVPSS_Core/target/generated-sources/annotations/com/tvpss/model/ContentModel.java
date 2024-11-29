package com.tvpss.model;

import java.util.List;

public class ContentModel {
    private String title;
    private String dateCreated;
    private String youtubeUrl;
    private String eventName;
    private String details;

    // Constructor, getters, and setters
    public ContentModel(String title, String dateCreated, String youtubeUrl, String eventName, String details) {
        this.title = title;
        this.dateCreated = dateCreated;
        this.youtubeUrl = youtubeUrl;
        this.eventName = eventName;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }
    
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    
}

