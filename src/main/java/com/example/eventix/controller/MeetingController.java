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
        // If it's a physical meeting, generate QR code
        if ("PHYSICAL".equalsIgnoreCase(meetingDTO.getMeetingType())) {
            String qrCode = qrCodeService.generateQRCode("Meeting ID: " + meetingDTO.getMeetingId(), 200, 200);
            meetingDTO.setQrCodeUrl(qrCode); // Set the generated QR code
        } else if ("ONLINE".equalsIgnoreCase(meetingDTO.getMeetingType())) {
            // If it's an online meeting, generate the VideoSDK meeting link
            String meetingLink = meetingService.createVideoSdkMeetingLink();
            meetingDTO.setMeetingLink(meetingLink); // Set the generated VideoSDK link
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
}
