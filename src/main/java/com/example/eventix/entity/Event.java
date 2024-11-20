package com.example.eventix.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private int budget_status; // -1: Pending, 0: Rejected, 1: Accepted

    private boolean public_status; // true for public, false for private

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Clubs club;


}
