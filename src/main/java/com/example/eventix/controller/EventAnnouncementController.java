package com.example.eventix.controller;

import com.example.eventix.dto.EventAnnouncementDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.EventAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/president")
@CrossOrigin(origins = "https://eventix-18.netlify.app")
public class EventAnnouncementController {

    @Autowired
    private EventAnnouncementService eventAnnouncementService;

    @GetMapping("/getAllEventAnnouncements")
    public ResponseEntity<ResponseDTO> getAllEventAnnouncements() {
        return ResponseEntity.ok().body(eventAnnouncementService.getAllEventAnnouncements());
    }

    @GetMapping("/getEventAnnouncement/{eventAnnouncementId}")
    public ResponseEntity<ResponseDTO> getEventAnnouncement(@PathVariable int eventAnnouncementId){
        return ResponseEntity.ok().body(eventAnnouncementService.getEventAnnouncement(eventAnnouncementId));
    }

    @PostMapping(value ="/saveEventAnnouncement", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> saveEventAnnouncement(@RequestBody EventAnnouncementDTO eventAnnouncementDTO){
        //return ResponseEntity.ok(announcementService.saveAnnouncement(announcementDTO));
        return ResponseEntity.ok().body(eventAnnouncementService.saveEventAnnouncement(eventAnnouncementDTO));
    }

    @PutMapping(value ="/updateEventAnnouncement/{eventAnnouncementId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> updateEventAnnouncement(@PathVariable int eventAnnouncementId, @RequestBody EventAnnouncementDTO eventAnnouncementDTO){
        return ResponseEntity.ok().body(eventAnnouncementService.updateEventAnnouncement(eventAnnouncementId, eventAnnouncementDTO));
    }

    @DeleteMapping("/deleteEventAnnouncement/{eventAnnouncementId}")
    public ResponseEntity<ResponseDTO> deleteEventAnnouncement(@PathVariable int eventAnnouncementId){
        return ResponseEntity.ok().body(eventAnnouncementService.deleteEventAnnouncement(eventAnnouncementId));
    }


}
