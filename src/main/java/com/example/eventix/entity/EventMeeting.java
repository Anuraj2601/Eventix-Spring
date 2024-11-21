package com.example.eventix.entity;

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
public class EventMeeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int e_meeting_id;

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
    private EventMeeting.MeetingType meeting_type;

    // Enum for the type of participants (EVERYONE, CLUB_MEMBERS, or CLUB_BOARD)
    public enum ParticipantType{
        EVERYONE,
        CLUB_MEMBERS,
        CLUB_BOARD
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventMeeting.ParticipantType participant_type;

    private String venue;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;



}
