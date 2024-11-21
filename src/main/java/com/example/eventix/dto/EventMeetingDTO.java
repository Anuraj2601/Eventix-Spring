package com.example.eventix.dto;

import com.example.eventix.entity.EventMeeting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventMeetingDTO {

    private int e_meeting_id;
    private String meeting_name;
    private LocalDate date;
    private LocalTime time;
    private EventMeeting.MeetingType meeting_type;
    private String venue;
    private int event_id;
    private int club_id;
}
