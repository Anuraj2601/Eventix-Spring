package com.example.eventix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "announcements")
public class Announcements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int announcement_id;
    private String title;
    private String content;

    public enum AnnouncementType{
        PUBLIC,
        ONLY_MEMBERS
    }
    @Enumerated(EnumType.STRING)
    private AnnouncementType type;

    @CreationTimestamp
    @Column(name = "date_posted", updatable = false)
    private LocalDateTime date_posted;

    @ManyToOne
    @JoinColumn(name = "club_id",nullable = false)
    private Clubs club;
}
