package com.example.eventix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clubs")
public class Clubs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int club_id;

    private String club_name;

    private String club_description;

    private String club_image;

    private boolean state;

    private boolean is_deleted;

    private LocalDateTime created_at;

    private String club_in_charge;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "club_president_id", referencedColumnName = "id")
    private Users users;

    @OneToMany(mappedBy = "clubs")
    private Set<Meeting> meetings;

    @OneToMany(mappedBy = "club")
    private Set<Announcements> announcements;

}
