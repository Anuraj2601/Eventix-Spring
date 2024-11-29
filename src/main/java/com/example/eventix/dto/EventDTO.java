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
    private String purpose;
    private String benefits;
    private String event_image;
    private String budget_pdf;

    private int iud_status;
    private int budget_status;

    private boolean public_status;

    private int club_id;
    private String clubImage;
    private String clubInCharge;
    private String clubName;

    private String clubPresidentImage;
    private String clubPresidentName;
}
