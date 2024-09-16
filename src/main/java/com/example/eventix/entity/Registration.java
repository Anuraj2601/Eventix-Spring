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
    @JoinColumn(name = "club_id")
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

    @PrePersist
    @PreUpdate
    private void updatePosition() {
        if (this.accepted == 1) {
            this.position = "member";
        }
    }
}
