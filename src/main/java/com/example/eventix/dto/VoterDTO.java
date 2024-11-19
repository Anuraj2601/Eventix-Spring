package com.example.eventix.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VoterDTO {
    private Long userId;
    private Long electionId;
    private boolean isVoted;
}
