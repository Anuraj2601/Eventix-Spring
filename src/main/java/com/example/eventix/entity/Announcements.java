package com.example.eventix.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
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
    @JsonBackReference
    @JoinColumn(name = "club_id",nullable = false)
    private Clubs club;
}
