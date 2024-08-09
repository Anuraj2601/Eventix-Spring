package com.example.eventix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}