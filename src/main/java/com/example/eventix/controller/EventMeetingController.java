package com.example.eventix.controller;

import com.example.eventix.dto.EventMeetingDTO;
import com.example.eventix.dto.EventOcDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.EventMeeting;
import com.example.eventix.service.EventMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/president")
@CrossOrigin(origins = "http://localhost:5173")
public class EventMeetingController {

    @Autowired
    private EventMeetingService eventMeetingService;

    @GetMapping("/getAllEventMeetings")
    public ResponseEntity<ResponseDTO> getAllEventMeetings() {
        return ResponseEntity.ok().body(eventMeetingService.getAllEventMeetings());
    }

    @GetMapping("/getEventMeeting/{e_meeting_id}")
    public ResponseEntity<ResponseDTO> getEventMeeting(@PathVariable int e_meeting_id){
        return ResponseEntity.ok().body(eventMeetingService.getEventMeeting(e_meeting_id));
    }

    @PostMapping(value ="/saveEventMeeting", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> saveEventMeeting(@RequestBody EventMeetingDTO eventMeetingDTO){
        //return ResponseEntity.ok(announcementService.saveAnnouncement(announcementDTO));
        return ResponseEntity.ok().body(eventMeetingService.saveEventMeeting(eventMeetingDTO));
    }

    @PutMapping(value ="/updateEventMeeting/{e_meeting_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> updateEventMeeting(@PathVariable int e_meeting_id, @RequestBody EventMeetingDTO eventMeetingDTO){
        return ResponseEntity.ok().body(eventMeetingService.updateEventMeeting(e_meeting_id, eventMeetingDTO));
    }

    @DeleteMapping("/deleteEventMeeting/{e_meeting_id}")
    public ResponseEntity<ResponseDTO> deleteEventMeeting(@PathVariable int e_meeting_id){
        return ResponseEntity.ok().body(eventMeetingService.deleteEventMeeting(e_meeting_id));
    }

}
