package com.tvpss.model;

import java.util.List;

public class ContentModel {
    private String title;
    private String dateCreated;
    private String youtubeUrl;
    private List<Content> additionalDetails;

    // Constructor, getters, and setters
    public ContentModel(String title, String dateCreated, String youtubeUrl, List<Content> additionalDetails) {
        this.title = title;
        this.dateCreated = dateCreated;
        this.youtubeUrl = youtubeUrl;
        this.additionalDetails = additionalDetails;
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

    public List<Content> getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(List<Content> additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    // Inner AdditionalDetails class to represent extra content details
    public static class Content {
        private String eventName;
        private String details;

        // Constructor, getters, and setters
        public Content(String eventName, String details) {
            this.eventName = eventName;
            this.details = details;
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
}

