package com.tvpss.model;

import java.util.ArrayList;
import java.util.List;

public class CrewModel {
    private int crewID;
    private String abilities;
    private String status;
    private String session;
    private String className;
    private int schoolID;
    private String userID;
    private List<UserModel> teachers; // List to store teacher details

    // Constructor
    public CrewModel(int crewID, String abilities, String status, String session, 
                     String className, int schoolID, String userID) {
        this.crewID = crewID;
        this.abilities = abilities;
        this.status = status;
        this.session = session;
        this.className = className;
        this.schoolID = schoolID;
        this.userID = userID;
        this.teachers = new ArrayList<>(); // Initialize the teachers list
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

    public List<UserModel> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<UserModel> teachers) {
        this.teachers = teachers;
    }

    // Method to add a single teacher to the teachers list
    public void addTeacher(UserModel teacher) {
        this.teachers.add(teacher);
    }

    // Method to add multiple teachers to the teachers list
    public void addTeachers(List<UserModel> teacherList) {
        this.teachers.addAll(teacherList);
    }
}
