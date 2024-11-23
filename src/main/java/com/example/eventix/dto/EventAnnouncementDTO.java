package com.example.eventix.dto;

import com.example.eventix.entity.EventAnnouncement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventAnnouncementDTO {

    private int e_announcement_id;
    private String title;
    private String content;
    private EventAnnouncement.EventAnnouncementType type;
    private LocalDateTime date_posted;
    private int club_id;
    private int event_id;
}
