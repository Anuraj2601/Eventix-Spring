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
    //private LocalTime time;
    private String purpose;
    private String benefits;
    private String event_image;
    private String budget_pdf;

    private boolean iud_status;
    private boolean budget_status;

//    @ManyToOne
//    @JoinColumn(name = "club_id")
//    private Clubs club;

}
