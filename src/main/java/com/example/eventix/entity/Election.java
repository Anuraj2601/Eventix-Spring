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
@Table(name = "elections")
public class Election {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int election_id;

//    @ManyToOne
//    @JoinColumn(name = "club_id", nullable = false)
//    private Clubs club;

    private String election_name;
    private LocalDateTime appOpens;
    private LocalDateTime appCloses;
    private LocalDateTime votingOpens;
    private LocalDateTime votingCloses;
    private boolean isAppClosed = false;
    private boolean isVotingClosed = false;

    @Column(name = "released", nullable = true, columnDefinition = "BIT DEFAULT 0")
    private Boolean released;


    @ManyToOne
    @JoinColumn(name = "club_id")
    private Clubs club;

    public int getElection_id() {
        return election_id;
    }

}
