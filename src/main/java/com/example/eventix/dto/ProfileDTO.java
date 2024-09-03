package com.example.eventix.dto;

public class ProfileDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String regNo;
    private String photoUrl;

    private String bio;

    // Default Constructor
    public ProfileDTO(String firstname, String lastname, String email, String regNo, String role, String bio, String photoUrl) {}

    // Parameterized Constructor
    public ProfileDTO(String firstname, String lastname, String email, String regNo, String photoUrl , String bio) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.regNo = regNo;
        this.photoUrl = photoUrl;
        this.bio = bio;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
