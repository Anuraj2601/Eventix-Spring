package com.example.eventix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contribution;
    private String position;
    private String userEmail;

    @ManyToOne
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;

    private Integer clubId; // Remove @Transient annotation

    @Column(name = "selected", nullable = false)
    private String selected = "applied"; // Default text value

    @Column(name = "votes", nullable = false)
    private Integer votes = 0; // Default value

    // Method to increment votes
    public void incrementVotes() {
        this.votes += 1;
    }
}
