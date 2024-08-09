package com.example.eventix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventDTO {
    private int event_id;
    private String name;
    private String venue;
    private LocalDate date;
    private LocalTime time;
    private double budget;
    private String purpose;
    private String benefits;
    private String imageUrl;
    private boolean IUDStatus;
    private String IUD_file;
}
