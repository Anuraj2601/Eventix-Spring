package com.example.eventix.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "meetings")
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int meeting_id;

    @Column(nullable = false)
    private String meeting_name;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    // Enum for the type of meeting (ONLINE or PHYSICAL)
    public enum MeetingType{
        ONLINE,
        PHYSICAL
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingType meeting_type;

    // Enum for the type of participants (EVERYONE, CLUB_MEMBERS, or CLUB_BOARD)
    public enum ParticipantType{
        EVERYONE,
        CLUB_MEMBERS,
        CLUB_BOARD
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipantType participant_type;

    // Relationship with Clubs entity, indicating each meeting belongs to a club
    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    @JsonBackReference
    private Clubs clubs;

    @Column(name = "qr_code_url", columnDefinition = "LONGTEXT")
    private String qrCodeUrl;  // URL for the QR code (for physical meetings)

    private String meetingLink;

    @Column(nullable = true)  // This is optional as String columns are nullable by default
    private String venue;

}
