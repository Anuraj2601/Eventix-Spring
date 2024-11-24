package com.example.eventix.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "meeting_participants")
public class MeetingParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int participantId;

    @Column(name = "meeting_id", nullable = false)
    private int meetingId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "club_id", nullable = false)
    private int clubId;

    @Column(name = "attendance", nullable = false, columnDefinition = "int default 0")
    private int attendance = 0;

    @Column(name = "qr_code_user", columnDefinition = "VARCHAR(255)")
    private String qrCodeUser;
}
