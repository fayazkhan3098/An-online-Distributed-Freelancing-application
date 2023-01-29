package com.example.m_ployer.model;

public class Users {

    private String email,profileImage,UId,name,status;

    public Users() {
    }

    public Users(String email, String profileImage, String UId, String name,String status) {
        this.email = email;
        this.profileImage = profileImage;
        this.UId = UId;
        this.name = name;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
