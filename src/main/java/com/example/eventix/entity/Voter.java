package com.example.eventix.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "voters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Voter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long electionId;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isVoted = true; // Default value set in Java

    public void setisVoted(boolean isVoted) {
        this.isVoted = isVoted;
    }

}
