package com.example.eventix.dto;

public class UserProfileDTO {
    private String name;
    private String profileImage;

    // Constructor
    public UserProfileDTO(String name, String profileImage) {
        this.name = name;
        this.profileImage = profileImage;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
