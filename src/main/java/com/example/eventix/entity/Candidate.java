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

    private Integer clubId;

    @Column(name = "selected", nullable = false)
    private String selected = "applied"; // Default text value

    @Column(name = "votes", nullable = false)
    private Integer votes = 0; // Default value

    // New fields
    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    // Method to increment votes
    public void incrementVotes() {
        this.votes += 1;
    }

    // Method to populate name and imageUrl based on userEmail

}
