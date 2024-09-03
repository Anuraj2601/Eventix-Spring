package com.example.eventix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int election_id;

//    @ManyToOne
//    @JoinColumn(name = "club_id", nullable = false)
//    private Clubs club;

private int memberId; // Renamed to camelCase

private int userId; // Renamed to camelCase

private String boardPosition; // Renamed to camelCase

private LocalDateTime joinedAt; // Renamed to camelCase

private String memberName; // Renamed to camelCase


    @ManyToOne
    @JoinColumn(name = "club_id")
    private Clubs club;
}
