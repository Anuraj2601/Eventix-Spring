package com.example.eventix.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class EventPost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private long id;
    private String title;


}
