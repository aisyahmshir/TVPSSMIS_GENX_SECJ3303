package com.tvpss.model;

import java.sql.Timestamp;

public class UserModel {
	private int id;
    private String userID;
    private String name;
    private String contactNo;
    private String email;
    private String status;
    private String role;
    private String password;
    private Timestamp lastActive;
    private String session;
    private int districtID;
    private int schoolID;

    // Constructor, Getters, and Setters
    public UserModel(int id, String userID, String name, String contactNo, String email, String status, 
                     String role, String password, Timestamp lastActive, String session, int districtID, int schoolID) {
        this.id = id;
        this.userID = userID;
        this.name = name;
        this.contactNo = contactNo;
        this.email = email;
        this.status = status;
        this.role = role;
        this.password = password;
        this.lastActive = lastActive;
        this.session = session;
        this.districtID = districtID;
        this.schoolID = schoolID;
    }

    // Getters and setters
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getLastActive() {
		return lastActive;
	}

	public void setLastActive(Timestamp lastActive) {
		this.lastActive = lastActive;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public int getDistrictID() {
		return districtID;
	}

	public void setDistrictID(int districtID) {
		this.districtID = districtID;
	}

	public int getSchoolID() {
		return schoolID;
	}

	public void setSchoolID(int schoolID) {
		this.schoolID = schoolID;
	}
}

