package com.example.eventix.controller;

import com.example.eventix.dto.MeetingDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.MeetingService;
import com.example.eventix.service.QRCodeService; // Add the service for QR code generation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/president")
@CrossOrigin(origins = "http://localhost:5173")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private QRCodeService qrCodeService; // Autowire QR code service

    @GetMapping("/getAllMeetings")
    public ResponseEntity<ResponseDTO> getAllMeetings() {
        return ResponseEntity.ok().body(meetingService.getAllMeetings());
    }

    @GetMapping("/getMeeting/{meeting_id}")
    public ResponseEntity<ResponseDTO> getMeeting(@PathVariable int meeting_id){
        return ResponseEntity.ok().body(meetingService.getMeeting(meeting_id));
    }

    @PostMapping("/saveMeeting")
    public ResponseEntity<ResponseDTO> saveMeeting(@RequestBody MeetingDTO meetingDTO) throws Exception {
        if ("PHYSICAL".equalsIgnoreCase(meetingDTO.getMeetingType())) {
            // Generate a unique file name for the QR code
            String fileName = "meeting-" + meetingDTO.getMeetingId();
            String qrCodeUrl = qrCodeService.generateQRCode("Meeting ID: " + meetingDTO.getMeetingId(), 200, 200, fileName);
            meetingDTO.setQrCodeUrl(qrCodeUrl); // Set the generated QR code URL
        } else if ("ONLINE".equalsIgnoreCase(meetingDTO.getMeetingType())) {
            String meetingLink = meetingService.createVideoSdkMeetingLink();
            meetingDTO.setMeetingLink(meetingLink);
        }
        return ResponseEntity.ok().body(meetingService.saveMeeting(meetingDTO));
    }

    @PutMapping(value="/updateMeeting/{meeting_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> updateMeeting(@PathVariable int meeting_id, @RequestBody MeetingDTO meetingDTO){
        return ResponseEntity.ok().body(meetingService.updateMeeting(meeting_id, meetingDTO));
    }

    @DeleteMapping("/deleteMeeting/{meeting_id}")
    public ResponseEntity<ResponseDTO> deleteMeeting(@PathVariable int meeting_id){
        return ResponseEntity.ok().body(meetingService.deleteMeeting(meeting_id));
    }

    @GetMapping("/joinOnlineMeeting/{meetingId}")
    public ResponseEntity<ResponseDTO> joinOnlineMeeting(@PathVariable int meetingId) {
        return ResponseEntity.ok().body(meetingService.joinOnlineMeeting(meetingId));
    }
}
