package com.example.eventix.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

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
    private String meeting_name;
    private LocalDate date;
    private LocalTime time;

   public enum MeetingType{
       ONLINE,
       PHYSICAL
   }

   @Enumerated(EnumType.STRING)
   private MeetingType meeting_type;

    public enum ParticipantType{
        EVERYONE,
        CLUB_MEMBERS,
        CLUB_BOARD
    }

    @Enumerated(EnumType.STRING)
    private ParticipantType participant_type;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    @JsonBackReference
    private Clubs clubs;

}
