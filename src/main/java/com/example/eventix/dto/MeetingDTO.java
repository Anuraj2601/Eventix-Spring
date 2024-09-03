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
}
