package com.tvpss.model;

public class ContentModel {
    private int contentID;        // Primary key, auto-increment
    private String title;         // VARCHAR(65)
    private String date;          // DATE
    private String eventName;     // VARCHAR(115)
    private String details;       // VARCHAR(250)
    private String createdAt;     // TIMESTAMP (default to current_timestamp())
    private String videoURL;      // VARCHAR(250)
    private int schoolID;         // Foreign key, index to school table

    // Constructor, getters, and setters
    public ContentModel(int contentID, String title, String date, String eventName, String details, String createdAt, String videoURL, int schoolID) {
        this.contentID = contentID;
        this.title = title;
        this.date = date;
        this.eventName = eventName;
        this.details = details;
        this.createdAt = createdAt;
        this.videoURL = videoURL;
        this.schoolID = schoolID;
    }

    public ContentModel() {
		// TODO Auto-generated constructor stub
	}

	public int getContentID() {
        return contentID;
    }

    public void setContentID(int contentID) {
        this.contentID = contentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public int getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(int schoolID) {
        this.schoolID = schoolID;
    }
}
