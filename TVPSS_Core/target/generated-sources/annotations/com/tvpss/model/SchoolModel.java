package com.tvpss.model;

import java.util.List;

import com.tvpss.model.CrewModel;

public class SchoolModel {
    private List<Session> sessions;

    // Constructor, getters, and setters
    public SchoolModel(List<Session> sessions) {
        this.sessions = sessions;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    // Inner Session class to represent session details
    public static class Session {
        private String sessionSchoolName;
        private String sessionTeacherInCharge;
        private String sessionSchoolPhone;
        private String sessionSchoolAddress;
        private String sessionSchoolPostcode;
        private String sessionSchoolDistrict;
        private String sessionTVPSSVersion;
        private String sessionStudioLevel;
        private String status;

        // Constructor, getters, and setters
        public Session(String sessionSchoolName, String sessionTeacherInCharge, String sessionSchoolPhone, String sessionSchoolAddress, String sessionSchoolPostcode, String sessionSchoolDistrict,
        		String sessionTVPSSVersion, String sessionStudioLevel) {
            this.sessionSchoolName = sessionSchoolName;
            this.sessionTeacherInCharge = sessionTeacherInCharge;
            this.sessionSchoolPhone = sessionSchoolPhone;
            this.sessionSchoolAddress = sessionSchoolAddress;
            this.sessionSchoolPostcode = sessionSchoolPostcode;
            this.sessionSchoolDistrict = sessionSchoolDistrict;
            this.sessionTVPSSVersion = sessionTVPSSVersion;
            this.sessionStudioLevel= sessionStudioLevel;
        }

     // Getters and Setters
        public String getSessionSchoolName() {
            return sessionSchoolName;
        }

        public void setSessionSchoolName(String sessionSchoolName) {
            this.sessionSchoolName = sessionSchoolName;
        }

        public String getSessionTeacherInCharge() {
            return sessionTeacherInCharge;
        }

        public void setSessionTeacherInCharge(String sessionTeacherInCharge) {
            this.sessionTeacherInCharge = sessionTeacherInCharge;
        }

        public String getSessionSchoolPhone() {
            return sessionSchoolPhone;
        }

        public void setSessionSchoolPhone(String sessionSchoolPhone) {
            this.sessionSchoolPhone = sessionSchoolPhone;
        }

        public String getSessionSchoolAddress() {
            return sessionSchoolAddress;
        }

        public void setSessionSchoolAddress(String sessionSchoolAddress) {
            this.sessionSchoolAddress = sessionSchoolAddress;
        }

        public String getSessionSchoolPostcode() {
            return sessionSchoolPostcode;
        }

        public void setSessionSchoolPostcode(String sessionSchoolPostcode) {
            this.sessionSchoolPostcode = sessionSchoolPostcode;
        }

        public String getSessionSchoolDistrict() {
            return sessionSchoolDistrict;
        }

        public void setSessionSchoolDistrict(String sessionSchoolDistrict) {
            this.sessionSchoolDistrict = sessionSchoolDistrict;
        }

        public String getSessionTVPSSVersion() {
            return sessionTVPSSVersion;
        }

        public void setSessionTVPSSVersion(String sessionTVPSSVersion) {
            this.sessionTVPSSVersion = sessionTVPSSVersion;
        }

        public String getSessionStudioLevel() {
            return sessionStudioLevel;
        }

        public void setSessionStudioLevel(String sessionStudioLevel) {
            this.sessionStudioLevel = sessionStudioLevel;
        }

    }
} 
