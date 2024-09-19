package com.example.eventix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ElectionDTO {
    private int election_id;
    private String election_name;
    private LocalDateTime appOpens;
    private LocalDateTime appCloses;
    private LocalDateTime votingOpens;
    private LocalDateTime votingCloses;
    private int club_id;
    private boolean isAppClosed;
    private boolean isVotingClosed;
    private Boolean released;
}
