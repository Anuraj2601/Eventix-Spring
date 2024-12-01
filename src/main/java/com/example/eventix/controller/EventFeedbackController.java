package com.example.eventix.controller;

import com.example.eventix.dto.EventFeedbackDTO;
import com.example.eventix.dto.EventMeetingDTO;
import com.example.eventix.dto.EventPostDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.EventFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "https://eventix-18.netlify.app")
public class EventFeedbackController {

    @Autowired
    private EventFeedbackService eventFeedbackService;

    @GetMapping("/getAllEventFeedbacks")
    public ResponseEntity<ResponseDTO> getAllEventFeedbacks() {
        return ResponseEntity.ok().body(eventFeedbackService.getAllEventFeedbacks());
    }

    @GetMapping("/getEventFeedback/{eventFeedbackId}")
    public ResponseEntity<ResponseDTO> getEventFeedback(@PathVariable int eventFeedbackId) {
        return ResponseEntity.ok().body(eventFeedbackService.getEventFeedback(eventFeedbackId));
    }

    @PostMapping(value ="/saveEventFeedback", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> saveEventFeedback(@RequestBody EventFeedbackDTO eventFeedbackDTO){
        //return ResponseEntity.ok(announcementService.saveAnnouncement(announcementDTO));
        return ResponseEntity.ok().body(eventFeedbackService.saveEventFeedback(eventFeedbackDTO));
    }

    @PutMapping(value ="/updateEventFeedback/{eventFeedbackId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> updateEventFeedback(@PathVariable int eventFeedbackId, @RequestBody EventFeedbackDTO eventFeedbackDTO){
        return ResponseEntity.ok().body(eventFeedbackService.updateEventFeedback(eventFeedbackId, eventFeedbackDTO));
    }

    @DeleteMapping("/deleteEventFeedback/{eventFeedbackId}")
    public ResponseEntity<ResponseDTO> deleteEventFeedback(@PathVariable int eventFeedbackId){
        return ResponseEntity.ok().body(eventFeedbackService.deleteEventFeedback(eventFeedbackId));
    }


}
