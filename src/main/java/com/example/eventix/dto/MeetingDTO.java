package com.example.eventix.dto;

import com.example.eventix.entity.Meeting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MeetingDTO {
    private int meeting_id;
    private String meeting_name;
    private LocalDate date;
    private LocalTime time;
    private Meeting.MeetingType meeting_type;
    private Meeting.ParticipantType participant_type;
    private int clubId;

    // Additional fields to handle QR code and VideoSDK link
    private String qrCodeUrl;  // To store the generated QR code URL for physical meetings
    private String meetingLink; // To store the VideoSDK meeting link for online meetings

    public String getMeetingType() {
        return meeting_type != null ? meeting_type.name() : null;  // Returns the name of the enum as a String
    }

    public String getMeetingId() {
        return String.valueOf(meeting_id);
    }
}
