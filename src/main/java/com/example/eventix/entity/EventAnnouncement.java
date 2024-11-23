package com.example.eventix.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "event_announcements")
public class EventAnnouncement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int e_announcement_id;

    private String title;

    private String content;

    public enum EventAnnouncementType {
        EVERYONE,
        OC
    }
    @Enumerated(EnumType.STRING)
    private EventAnnouncementType type;

    @CreationTimestamp
    @Column(name = "date_posted", updatable = false)
    private LocalDateTime date_posted;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;


    @ManyToOne
    @JoinColumn(name = "club_id")
    private Clubs club;

}
