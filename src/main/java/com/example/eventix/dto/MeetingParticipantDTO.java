package com.example.eventix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MeetingParticipantDTO {

    private int participantId;
    private int meetingId;
    private int userId;
    private int clubId;
    private int attendance;
    private String qrCodeUser;  // Optional, depending on if you want to display it.
}
