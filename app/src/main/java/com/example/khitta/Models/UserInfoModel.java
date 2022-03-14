package com.example.khitta.Models;

public class UserInfoModel {
    String profileName, profileEmail, profilePhoneNo, profileGender, profileAddress, imagePath,
            profileFatherName, profileCnic, profileDOB;

    public UserInfoModel() {
    }

    public UserInfoModel(String profileName, String profileEmail, String profilePhoneNo, String profileGender, String profileAddress,
                         String imagePath, String profileFatherName, String profileCnic, String profileDOB) {
        this.profileName = profileName;
        this.profileEmail = profileEmail;
        this.profilePhoneNo = profilePhoneNo;
        this.profileGender = profileGender;
        this.profileAddress = profileAddress;
        this.imagePath = imagePath;
        this.profileFatherName = profileFatherName;
        this.profileCnic = profileCnic;
        this.profileDOB = profileDOB;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileEmail() {
        return profileEmail;
    }

    public void setProfileEmail(String profileEmail) {
        this.profileEmail = profileEmail;
    }

    public String getProfilePhoneNo() {
        return profilePhoneNo;
    }

    public void setProfilePhoneNo(String profilePhoneNo) {
        this.profilePhoneNo = profilePhoneNo;
    }

    public String getProfileGender() {
        return profileGender;
    }

    public void setProfileGender(String profileGender) {
        this.profileGender = profileGender;
    }

    public String getProfileAddress() {
        return profileAddress;
    }

    public void setProfileAddress(String profileAddress) {
        this.profileAddress = profileAddress;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getProfileFatherName() {
        return profileFatherName;
    }

    public void setProfileFatherName(String profileFatherName) {
        this.profileFatherName = profileFatherName;
    }

    public String getProfileCnic() {
        return profileCnic;
    }

    public void setProfileCnic(String profileCnic) {
        this.profileCnic = profileCnic;
    }

    public String getProfileDOB() {
        return profileDOB;
    }

    public void setProfileDOB(String profileDOB) {
        this.profileDOB = profileDOB;
    }
}