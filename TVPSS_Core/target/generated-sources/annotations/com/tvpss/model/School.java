package com.tvpss.model;

public class School {

    private Long schoolID;
    private String name;
    private String state;
    private String fullAddress;
    private String contactNo;
    private String versionImageURL;
    private byte[] logo; // For BLOB data
    private byte[] schoolPic; // For BLOB data
    private Long districtID;
	private int tvpssVersion;
	
	
	
    public int getTvpssVersion() {
		return tvpssVersion;
	}

	public void setTvpssVersion(int tvpssVersion) {
		this.tvpssVersion = tvpssVersion;
	}



    // Getters and Setters
    public Long getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(Long schoolID) {
        this.schoolID = schoolID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public byte[] getSchoolPic() {
        return schoolPic;
    }

    public void setSchoolPic(byte[] schoolPic) {
        this.schoolPic = schoolPic;
    }

    public Long getDistrictID() {
        return districtID;
    }

    public void setDistrictID(Long districtID) {
        this.districtID = districtID;
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

