package com.example.eventix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClubsDTO {
    private int club_id;
    private String club_name;
    private String club_description;
    private String club_image;
    private boolean state;
    private boolean is_deleted;
    private LocalDateTime created_at;
    private String club_in_charge;
    private Set<MeetingDTO> meetings;
    private Set<AnnouncementDTO> announcements;
    private int club_president_id;
//    private int club_secretary_id;
//    private int club_treasurer_id;
}
