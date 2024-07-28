package com.example.eventix.controller;

import com.example.eventix.dto.MeetingDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/president")
@CrossOrigin(origins = "http://localhost:5173")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @GetMapping("/getAllMeetings")
    public ResponseEntity<ResponseDTO> getAllMeetings() {
        return ResponseEntity.ok().body(meetingService.getAllMeetings());
    }

    @GetMapping("/getMeeting/{meeting_id}")
    public ResponseEntity<ResponseDTO> getMeeting(@PathVariable int meeting_id){
        return ResponseEntity.ok().body(meetingService.getMeeting(meeting_id));
    }

    @PostMapping("/saveMeeting")
    public ResponseEntity<ResponseDTO> saveMeeting(@RequestBody MeetingDTO meetingDTO){
        return ResponseEntity.ok().body(meetingService.saveMeeting(meetingDTO));
    }

    @PutMapping("/updateMeeting")
    public ResponseEntity<ResponseDTO> updateMeeting(@RequestBody MeetingDTO meetingDTO){
        return ResponseEntity.ok().body(meetingService.updateMeeting(meetingDTO));
    }

    @DeleteMapping("/deleteMeeting/{meeting_id}")
    public ResponseEntity<ResponseDTO> deleteMeeting(@PathVariable int meeting_id){
        return ResponseEntity.ok().body(meetingService.deleteMeeting(meeting_id));
    }

}
