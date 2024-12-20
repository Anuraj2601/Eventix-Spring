package com.example.eventix.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

//    private int club_president_id;

//    @ToString.Exclude
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "club_president_id", referencedColumnName = "id")
//    @JsonBackReference
//    private Users users;

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "club_president_id", referencedColumnName = "id")
    @JsonBackReference
    private Users president; // Renamed for clarity

//    @ToString.Exclude
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "club_secretary_id", referencedColumnName = "id")
//    @JsonBackReference
//    private Users secretary; // Secretary field
//
//    @ToString.Exclude
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "club_treasurer_id", referencedColumnName = "id")
//    @JsonBackReference
//    private Users treasurer; // Treasurer field

    @OneToMany(mappedBy = "clubs", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Meeting> meetings;

    @OneToMany(mappedBy = "club", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Announcements> announcements;

}
