package com.tvpss.model;

import java.util.List;

public class CrewModel {
	private int crewID;
    private String abilities;
    private String status;
    private String session;
    private String className;
    private int schoolID;
    private String userID;

    // Constructor, Getters, and Setters
    public CrewModel(int crewID, String abilities, String status, String session, 
                     String className, int schoolID, String userID) {
        this.crewID = crewID;
        this.abilities = abilities;
        this.status = status;
        this.session = session;
        this.className = className;
        this.schoolID = schoolID;
        this.userID = userID;
    }

    // Getters and setters
    public int getCrewID() {
		return crewID;
	}

	public void setCrewID(int crewID) {
		this.crewID = crewID;
	}

	public String getAbilities() {
		return abilities;
	}

	public void setAbilities(String abilities) {
		this.abilities = abilities;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getSchoolID() {
		return schoolID;
	}

	public void setSchoolID(int schoolID) {
		this.schoolID = schoolID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
}

