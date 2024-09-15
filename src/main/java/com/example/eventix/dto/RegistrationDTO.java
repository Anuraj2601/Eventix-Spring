package com.example.eventix.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegistrationDTO {
    private int registration_id; // Use registration_id to match the field name in entity
    private String fullName;
    private String email;
    private String registerNo;
    private String indexNo;
    private String team;
    private String yearOfStudy;
    private LocalDateTime interviewSlot;
    private String reason;
    private int club_id; // Ensure this matches the field in the Clubs entity
    private int accepted; // Add this line to the DTO
}
