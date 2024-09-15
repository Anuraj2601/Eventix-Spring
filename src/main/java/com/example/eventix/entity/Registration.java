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
    @Column(name = "registration_id") // Use registration_id for the column name
    private int registration_id; // Field name should match column name

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "register_no")
    private String registerNo;

    @Column(name = "index_no")
    private String indexNo;

    @Column(name = "team")
    private String team;

    @Column(name = "year_of_study")
    private String yearOfStudy;

    @Column(name = "interview_slot")
    private LocalDateTime interviewSlot;

    @Lob
    @Column(name = "reason")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    @JsonBackReference
    private Clubs club;

    @Column(name = "accepted", nullable = false, columnDefinition = "int default 0")
    private int accepted = 0; // Default value set to 0
}
