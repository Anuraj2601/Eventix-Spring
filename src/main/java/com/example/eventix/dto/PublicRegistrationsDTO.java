package com.example.eventix.dto;

import java.time.LocalDateTime;

public class PublicRegistrationsDTO {
    private Long id;
    private Long eventId; // Event ID (simple field)
    private Long clubId; // Club ID (simple field)
    private String eventName; // Event Name
    private String participantName;
    private String email;

    private int checkInStatus; // 0 for not checked in, 1 for checked in


    private String mobile;
    private LocalDateTime registrationTime;

    // Constructors, getters, and setters
    public PublicRegistrationsDTO(int checkInStatus ,Long id,Long eventId, Long clubId, String eventName, String participantName, String email, String mobile, LocalDateTime registrationTime) {
        this.id = id;
        this.eventId = eventId;
        this.clubId = clubId;
        this.eventName = eventName;
        this.participantName = participantName;
        this.email = email;
        this.mobile = mobile;
        this.registrationTime = registrationTime;
        this.checkInStatus = checkInStatus;

    }

    public int getCheckInStatus() {
        return checkInStatus;
    }

    // Correct setter to accept int instead of Long
    public void setCheckInStatus(int checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }
}
