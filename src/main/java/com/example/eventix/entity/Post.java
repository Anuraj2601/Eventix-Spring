package com.example.eventix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int post_id;
    private String name;
    private String position;
    private String description;
    private String post_image;

    public enum postType{
        PENDING,
        APPROVED,
        REJECTED
    }

    @Enumerated(EnumType.STRING)
    private postType post_status;

    @CreationTimestamp
    @Column(name = "date_posted", updatable = false)
    private LocalDateTime date_posted;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Clubs club;
}
