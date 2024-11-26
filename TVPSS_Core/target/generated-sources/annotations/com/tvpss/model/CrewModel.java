package com.tvpss.model;

import java.util.List;

public class CrewModel {
    private String studentName;
    private String studentEmail;
    private List<Session> sessions;

    // Constructor, getters, and setters
    public CrewModel(String studentName, String studentEmail, List<Session> sessions) {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.sessions = sessions;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    // Inner Session class to represent session details
    public static class Session {
        private String sessionName;
        private String sessionEmail;
        private String status;

        // Constructor, getters, and setters
        public Session(String sessionName, String sessionEmail, String status) {
            this.sessionName = sessionName;
            this.sessionEmail = sessionEmail;
            this.status = status;
        }

        public String getSessionName() {
            return sessionName;
        }

        public void setSessionName(String sessionName) {
            this.sessionName = sessionName;
        }

        public String getSessionEmail() {
            return sessionEmail;
        }

        public void setSessionEmail(String sessionEmail) {
            this.sessionEmail = sessionEmail;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
