package com.example.eventix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "event_oc")
public class EventOc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int oc_id;
    private String oc_name;
    private String team;
    private String event_name;
    private boolean is_removed;

    // Many EventOc entries can refer to the same User (one user, multiple OCs)
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false)
    private Users member;

    // Many EventOc entries can refer to the same Event (one event, multiple OCs)
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;



}
