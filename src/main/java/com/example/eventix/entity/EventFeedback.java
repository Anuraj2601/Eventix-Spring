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
@Table(name = "event_feedback")
public class EventFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int e_feedback_id;

    private String feedback;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false)
    private Users student;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;


    @ManyToOne
    @JoinColumn(name = "club_id")
    private Clubs club;

    @CreationTimestamp
    @Column(name = "date_posted", updatable = false)
    private LocalDateTime date_posted;

}
