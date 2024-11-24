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
    private final QRCodeService qrCodeService;
    private final MeetingParticipantRepository participantRepository;

    @Autowired
    public MeetingParticipantService(QRCodeService qrCodeService, MeetingParticipantRepository participantRepository) {
        this.qrCodeService = qrCodeService;
        this.participantRepository = participantRepository;
    }

    // Method to handle QR code generation after participant is inserted
    @Transactional
    public void generateQRCodeForNewParticipant(Long participantId) {
        MeetingParticipant participant = participantRepository.findById(participantId).orElseThrow(() -> new RuntimeException("Participant not found"));

        // Generate the QR code content based on the userId, clubId, and meetingId
        String content = participant.getUserId() + "-" + participant.getClubId() + "-" + participant.getMeetingId();

        try {
            // Generate QR code image and get the URL
            String qrCodeUrl = qrCodeService.generateQRCode(content, 200, 200, "meeting-participant-" + participant.getUserId() + "-" + participant.getClubId() + "-" + participant.getMeetingId());

            // Update the participant record with the QR code URL
            participant.setQrCodeUrl(qrCodeUrl);
            participantRepository.save(participant);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions, such as error in QR code generation
        }
    }
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
