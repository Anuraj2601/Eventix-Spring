package com.example.eventix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "public_registrations")
public class PublicRegistrations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId; // Event ID (simple field, no relationship)
    private Long clubId; // Club ID (simple field, no relationship)
    private String eventName; // Event Name
    private String participantName;
    private String email;
    private String mobile;
    private LocalDateTime registrationTime;
    private int checkInStatus; // 0 for not checked in, 1 for checked in

    // Constructor with parameters
    public PublicRegistrations(Long eventId, Long clubId, String eventName, String participantName, String email, String mobile, LocalDateTime registrationTime, int checkInStatus) {
        this.eventId = eventId;
        this.clubId = clubId;
        this.eventName = eventName;
        this.participantName = participantName;
        this.email = email;
        this.mobile = mobile;
        this.registrationTime = registrationTime;
        this.checkInStatus = checkInStatus;
    }
}
