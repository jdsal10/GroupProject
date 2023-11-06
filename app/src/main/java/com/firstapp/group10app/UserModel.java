package com.firstapp.group10app;

public class UserModel {
    private String userID;
    private String email;
    private String preferredName;
    private String password;
    private String DOB;
    private float weight;
    private float height;
    private String gender;
    private String medicalConditions;
    private String reasonsForDownloading;

    public UserModel(String userID, String email, String preferredName, String password, String DOB, float weight, float height, String gender, String medicalConditions, String reasonsForDownloading) {
        this.userID = userID;
        this.email = email;
        this.preferredName = preferredName;
        this.password = password;
        this.DOB = DOB;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
        this.medicalConditions = medicalConditions;
        this.reasonsForDownloading = reasonsForDownloading;
    }

    public UserModel() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(String medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public String getReasonsForDownloading() {
        return reasonsForDownloading;
    }

    public void setReasonsForDownloading(String reasonsForDownloading) {
        this.reasonsForDownloading = reasonsForDownloading;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userID='" + userID + '\'' +
                ", email='" + email + '\'' +
                ", preferredName='" + preferredName + '\'' +
                ", password='" + password + '\'' +
                ", DOB='" + DOB + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", gender='" + gender + '\'' +
                ", medicalConditions='" + medicalConditions + '\'' +
                ", reasonsForDownloading='" + reasonsForDownloading + '\'' +
                '}';
    }
}