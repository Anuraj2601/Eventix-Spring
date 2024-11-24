package com.example.eventix.service;

import com.example.eventix.dto.MeetingParticipantDTO;
import com.example.eventix.entity.MeetingParticipant;
import com.example.eventix.repository.MeetingParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingParticipantService {

    @Autowired
    private MeetingParticipantRepository participantRepository;

    // Fetch all participants for a specific meeting
    public List<MeetingParticipantDTO> getAllParticipants(int meetingId) {
        List<MeetingParticipant> participants = participantRepository.findByMeetingId(meetingId);
        return participants.stream()
                .map(participant -> new MeetingParticipantDTO(
                        participant.getParticipantId(),
                        participant.getMeetingId(),
                        participant.getUserId(),
                        participant.getClubId(),
                        participant.getAttendance(),
                        participant.getQrCodeUser()))
                .collect(Collectors.toList());
    }

    // Update attendance for a specific user and meeting
    public String updateAttendance(int userId, int meetingId, int attendanceStatus) {
        MeetingParticipant participant = participantRepository.findByUserIdAndMeetingId(userId, meetingId);
        if (participant != null) {
            participant.setAttendance(attendanceStatus);
            participantRepository.save(participant);  // Update participant
            return "Attendance updated successfully";
        } else {
            return "Participant not found";
        }
    }
}
