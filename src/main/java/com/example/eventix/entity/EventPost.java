package com.example.eventix.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "event_posts")
public class EventPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int e_post_id;

    private String title;

    private String description;

    private String post_image;

    public enum eventPostType {
        PENDING,
        APPROVED,
        REJECTED
    }

    @Enumerated(EnumType.STRING)
    private eventPostType post_status = eventPostType.PENDING;

    @CreationTimestamp
    @Column(name = "date_posted", updatable = false)
    private LocalDateTime date_posted;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Clubs club;

    @ManyToOne
    @JoinColumn(name = "published_user_id", referencedColumnName = "id", updatable = false)   //published user id
    private Users published_user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
