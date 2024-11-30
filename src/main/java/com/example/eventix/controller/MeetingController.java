package com.example.eventix.controller;

import com.example.eventix.dto.EmailRequest;
import com.example.eventix.dto.MeetingDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.EmailService;
import com.example.eventix.service.MeetingService;
import com.example.eventix.service.QRCodeService; // Add the service for QR code generation
import com.example.eventix.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private EmailService emailService;

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

//    @PostMapping("/sendQrCode/{meetingId}")
//    public ResponseEntity<String> sendQrCode(
//            @PathVariable int meetingId,
//            @RequestBody EmailRequest emailRequest,
//            @RequestHeader("Authorization") String token) {
//        try {
//            meetingService.sendQrCodeToUser(meetingId, emailRequest.getEmail());
//            return ResponseEntity.ok("QR Code sent successfully!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send QR Code: " + e.getMessage());
//        }
//    }

    @PostMapping("/sendQrCode/{meetingId}")
    public ResponseEntity<ResponseMessage> sendQrCode(@PathVariable Long meetingId,
                                                      @RequestBody SendQrCodeRequest request) {
        try {
            emailService.sendQrCodeEmail(request.getEmail(), request.getQrCodeDataUrls(), meetingId);
            return ResponseEntity.ok(new ResponseMessage("QR Code sent successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage("Error sending QR code: " + e.getMessage()));
        }
    }

    @PostMapping("/sendMeetingCode/{meetingId}")
    public ResponseEntity<String> sendMeetingCode(
            @PathVariable int meetingId,
            @RequestBody EmailRequest emailRequest,
            @RequestHeader("Authorization") String token) {
        try {
            meetingService.sendMeetingCodeToUser(meetingId, emailRequest.getEmail());
            return ResponseEntity.ok("Meeting Code sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send Meeting Code: " + e.getMessage());
        }
    }
}
