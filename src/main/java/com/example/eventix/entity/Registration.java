package com.example.eventix.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "registrations")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    private int registrationId;

    @Column(name = "team")
    private String team;

    @Column(name = "interview_slot")
    private LocalDateTime interviewSlot;

    @Lob
    @Column(name = "reason")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id") // Ensure this column matches with the field name in Clubs
    @JsonBackReference
    private Clubs club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    @JsonBackReference
    private Users user;

    @Column(name = "accepted", nullable = false, columnDefinition = "int default 0")
    private int accepted = 0;

    @Column(name = "position", nullable = false, columnDefinition = "varchar(255) default 'student'")
    private String position = "student";

    @Column(name = "created_at", nullable = true, updatable = false)
    private LocalDateTime createdAt;

    // New columns
    @Column(name = "oc", columnDefinition = "json", nullable = true)
    private String oc;  // JSON array will be stored as a String (to handle JSON in database)

    @Column(name = "performance", nullable = true)
    private Integer performance; // Performance as an integer, nullable


    @PrePersist
    private void onCreate() {
        // Set the createdAt field to the current time before inserting the record if it's not already set
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }

        // Ensure position is set to "member" if accepted is 1
        if (this.accepted == 1) {
            this.position = "member";
        }
    }
    @PreUpdate
    private void updatePosition() {
        if (this.accepted == 1) {
            this.position = "member";
        }
    }
}
