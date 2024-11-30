package com.example.eventix.repository;

import com.example.eventix.entity.MeetingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingParticipantRepository extends JpaRepository<MeetingParticipant, Integer> {

    // Fetch all participants for a specific meeting
    List<MeetingParticipant> findByMeetingId(int meetingId);

    // Fetch a participant by userId and meetingId
    MeetingParticipant findByUserIdAndMeetingId(int userId, int meetingId);

    List<MeetingParticipant> findByUserIdAndClubId(int userId, int clubId);


}
