package com.example.eventix.dto;

import java.time.LocalDateTime;

public class PublicRegistrationsDTO {

    private Long eventId; // Event ID (simple field)
    private Long clubId; // Club ID (simple field)
    private String eventName; // Event Name
    private String participantName;
    private String email;

    private String mobile;
    private LocalDateTime registrationTime;

    // Constructors, getters, and setters
    public PublicRegistrationsDTO(Long eventId, Long clubId, String eventName, String participantName, String email, String mobile, LocalDateTime registrationTime) {
        this.eventId = eventId;
        this.clubId = clubId;
        this.eventName = eventName;
        this.participantName = participantName;
        this.email = email;
        this.mobile = mobile;
        this.registrationTime = registrationTime;
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