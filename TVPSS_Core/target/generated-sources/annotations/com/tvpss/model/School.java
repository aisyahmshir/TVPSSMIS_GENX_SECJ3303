package com.tvpss.model;

public class School {



	private int schoolID;
    private String name;
    private UserModel teacher;
    private String state;
    private String fullAddress;
    private String contactNo;
    private String versionImageURL;
    private byte[] logo; // For BLOB data
    private int districtID;
	private int tvpssVersion;
	private int studioID;
	
	public School() {}
    public School(String name, String fullAddress, String state, int districtID, String contactNo, String versionImageURL) {
        this.name = name;
        this.fullAddress = fullAddress;
        this.state = state;
        this.districtID = districtID;
        this.contactNo = contactNo;
        this.versionImageURL = versionImageURL;
    }
	
	
	
    public School(int schoolID, String name, UserModel teacher, String state, String fullAddress, String contactNo,
			String versionImageURL, int districtID, int tvpssVersion, int studioID) {
		super();
		this.schoolID = schoolID;
		this.name = name;
		this.teacher = teacher;
		this.state = state;
		this.fullAddress = fullAddress;
		this.contactNo = contactNo;
		this.versionImageURL = versionImageURL;
		this.districtID = districtID;
		this.tvpssVersion = tvpssVersion;
		this.studioID = studioID;
	}
	
    public int getTvpssVersion() {
		return tvpssVersion;
	}

	public void setTvpssVersion(int tvpssVersion) {
		this.tvpssVersion = tvpssVersion;
	}



    // Getters and Setters
    public int getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(int schoolID) {
        this.schoolID = schoolID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserModel getTeacher() {
        return teacher;
    }

    public void setTeacher(UserModel setTeacher) {
        this.teacher = teacher;
    }
    
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getVersionImageURL() {
        return versionImageURL;
    }

    public void setVersionImageURL(String versionImageURL) {
        this.versionImageURL = versionImageURL;
    }

    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }

    public int getStudioID() {
        return studioID;
    }

    public void setStudioID(int studioID) {
        this.studioID = studioID;
    }
    
    @Override
    public String toString() {
        return "School{" +
                "schoolID=" + schoolID +
                ", name='" + name + '\'' +
                ", state='" + state + '\'' +
                ", fullAddress='" + fullAddress + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", versionImageURL='" + versionImageURL + '\'' +
                ", districtID=" + districtID +
                '}';
    }
}

