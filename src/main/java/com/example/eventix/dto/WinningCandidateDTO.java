package com.example.eventix.dto;

public class WinningCandidateDTO {
    private String userId; // Assuming this is the email or ID of the user
    private String position; // The position the candidate won
    private int clubId; // The ID of the club associated with the election

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }
}
