package com.example.eventix.controller;

import com.example.eventix.dto.MeetingParticipantDTO;
import com.example.eventix.service.MeetingParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meeting-participants")
public class MeetingParticipantController {

    @Autowired
    private MeetingParticipantService participantService;

    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<List<MeetingParticipantDTO>> getAllParticipants(@PathVariable int meetingId) {
        List<MeetingParticipantDTO> participants = participantService.getAllParticipants(meetingId);
        return ResponseEntity.ok(participants);
    }

    // Endpoint to update attendance for a specific user and meeting
    @PatchMapping("/attendance")
    public ResponseEntity<String> updateAttendance(@RequestParam int userId, @RequestParam int meetingId, @RequestParam int attendanceStatus) {
        String responseMessage = participantService.updateAttendance(userId, meetingId, attendanceStatus);
        return ResponseEntity.ok(responseMessage);
    }
}
