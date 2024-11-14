package com.example.eventix.dto;

import java.time.LocalDateTime;

public class RegistrationDTO {

    private int registrationId;
    private String email; // Changed from userId to email
    private int clubId;
    private String team;
    private LocalDateTime interviewSlot;
    private String reason;
    private int accepted;
    private String position;
    private int userId;

    private LocalDateTime createdAt;

    // Getters and setters

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public LocalDateTime getInterviewSlot() {
        return interviewSlot;
    }

    public void setInterviewSlot(LocalDateTime interviewSlot) {
        this.interviewSlot = interviewSlot;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;

    }
}
