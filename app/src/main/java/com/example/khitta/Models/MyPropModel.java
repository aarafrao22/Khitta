package com.example.khitta.Models;

public class MyPropModel {
    private String property_area,property_areaSize,property_bedrooms,property_description,
            property_location,property_purpose,property_title,property_type,property_price,path,
            property_uEmail,property_uName,property_uPhoneNo,property_washrooms,date,userId;

    public MyPropModel() {
    }

    public MyPropModel(String property_area, String property_areaSize, String property_bedrooms, String property_description, String property_location, String property_purpose, String property_title, String property_type, String property_price, String path, String property_uEmail, String property_uName,
                       String property_uPhoneNo, String property_washrooms, String date, String userId) {
        this.property_area = property_area;
        this.property_areaSize = property_areaSize;
        this.property_bedrooms = property_bedrooms;
        this.property_description = property_description;
        this.property_location = property_location;
        this.property_purpose = property_purpose;
        this.property_title = property_title;
        this.property_type = property_type;
        this.property_price = property_price;
        this.path = path;
        this.property_uEmail = property_uEmail;
        this.property_uName = property_uName;
        this.property_uPhoneNo = property_uPhoneNo;
        this.property_washrooms = property_washrooms;
        this.date = date;
        this.userId = userId;
    }

    public String getProperty_area() {
        return property_area;
    }

    public void setProperty_area(String property_area) {
        this.property_area = property_area;
    }

    public String getProperty_areaSize() {
        return property_areaSize;
    }

    public void setProperty_areaSize(String property_areaSize) {
        this.property_areaSize = property_areaSize;
    }

    public String getProperty_bedrooms() {
        return property_bedrooms;
    }

    public void setProperty_bedrooms(String property_bedrooms) {
        this.property_bedrooms = property_bedrooms;
    }

    public String getProperty_description() {
        return property_description;
    }

    public void setProperty_description(String property_description) {
        this.property_description = property_description;
    }

    public String getProperty_location() {
        return property_location;
    }

    public void setProperty_location(String property_location) {
        this.property_location = property_location;
    }

    public String getProperty_purpose() {
        return property_purpose;
    }

    public void setProperty_purpose(String property_purpose) {
        this.property_purpose = property_purpose;
    }

    public String getProperty_title() {
        return property_title;
    }

    public void setProperty_title(String property_title) {
        this.property_title = property_title;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getProperty_price() {
        return property_price;
    }

    public void setProperty_price(String property_price) {
        this.property_price = property_price;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProperty_uEmail() {
        return property_uEmail;
    }

    public void setProperty_uEmail(String property_uEmail) {
        this.property_uEmail = property_uEmail;
    }

    public String getProperty_uName() {
        return property_uName;
    }

    public void setProperty_uName(String property_uName) {
        this.property_uName = property_uName;
    }

    public String getProperty_uPhoneNo() {
        return property_uPhoneNo;
    }

    public void setProperty_uPhoneNo(String property_uPhoneNo) {
        this.property_uPhoneNo = property_uPhoneNo;
    }

    public String getProperty_washrooms() {
        return property_washrooms;
    }

    public void setProperty_washrooms(String property_washrooms) {
        this.property_washrooms = property_washrooms;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
