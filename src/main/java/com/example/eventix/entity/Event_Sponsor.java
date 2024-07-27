package com.example.eventix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "event_sponsor")
public class Event_Sponsor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sponsor_id;

    private String sponsor_name;

    private String company_logo;

    public enum SponsorType {
        GOLD,
        SILVER,
        PLATINUM
    }
    @Enumerated(EnumType.STRING)
    private SponsorType sponsorType;

    private String sponsor_description;

    private String contact_person;

    private String contact_email;

    @Column(columnDefinition = "int(11) default '0' ")
    private int amount;
}
