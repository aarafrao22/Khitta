package com.example.khitta.Models;

public class PostsModel {
    private String property_area,property_areaSize,property_bedrooms,property_description,
            property_location,property_purpose,property_title,property_type,property_price,path,path2,path3,
            property_uEmail,property_uName,property_uPhoneNo,property_washrooms,date,userId;

    public PostsModel() {
    }

    public PostsModel(String property_area, String property_areaSize, String property_bedrooms,
                      String property_description, String property_location, String property_purpose,
                      String property_title, String property_type, String property_price, String path,
                      String path1,String path2,
                      String property_uEmail, String property_uName,String property_uPhoneNo,
                      String property_washrooms, String userId)
    {
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
        this.path2 = path2;
        this.path3 = path3;
        this.property_uEmail = property_uEmail;
        this.property_uName = property_uName;
        this.property_uPhoneNo = property_uPhoneNo;
        this.property_washrooms = property_washrooms;
        this.userId = userId;
    }

    public String getProperty_area() {
        return property_area;
    }

    public String getProperty_areaSize() {
        return property_areaSize;
    }

    public String getProperty_bedrooms() {
        return property_bedrooms;
    }

    public String getProperty_description() {
        return property_description;
    }

    public String getProperty_location() {
        return property_location;
    }

    public String getProperty_purpose() {
        return property_purpose;
    }

    public String getProperty_price() {
        return property_price;
    }

    public String getProperty_title() {
        return property_title;
    }

    public String getProperty_type() {
        return property_type;
    }

    public String getPath() {
        return path;
    }

    public String getPath2() {
        return path2;
    }

    public String getPath3() {
        return path3;
    }

    public String getProperty_uEmail() {
        return property_uEmail;
    }

    public String getProperty_uName() {
        return property_uName;
    }

    public String getProperty_uPhoneNo() {
        return property_uPhoneNo;
    }

    public String getProperty_washrooms() {
        return property_washrooms;
    }

    public String getDate() {
        return date;
    }

    public String getUserId() {
        return userId;
    }
}
