package com.example.eventix.dto;

import com.example.eventix.entity.Announcements;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnnouncementDTO {
    private int announcement_id;
    private String title;
    private String content;
    private Announcements.AnnouncementType type = Announcements.AnnouncementType.ONLY_MEMBERS;
    private LocalDateTime date_posted;
}
