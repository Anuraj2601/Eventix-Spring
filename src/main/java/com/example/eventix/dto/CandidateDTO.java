package com.example.eventix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO {

    private Long id;
    private String contribution;
    private String position;
    private String userEmail;
    private Integer electionId; // Foreign key from Election
    private Integer clubId;     // Derived from the Election entity's Club
    private String selected;    // New field
    private Integer votes;      // New field
    private String name;        // New field
    private String imageUrl;    // New field
    private String oc;          // New field to store JSON data for event names
    private Integer performance; // New field to store performance percentage or integer
}
