package com.example.eventix.controller;

import com.example.eventix.dto.AnnouncementDTO;
import com.example.eventix.dto.EventAnnouncementDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.EventAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/president")
@CrossOrigin(origins = "http://localhost:5173")
public class EventAnnouncementController {

    @Autowired
    private EventAnnouncementService eventAnnouncementService;

    @GetMapping("/getAllEventAnnouncements")
    public ResponseEntity<ResponseDTO> getAllEventAnnouncements() {
        return ResponseEntity.ok().body(eventAnnouncementService.getAllEventAnnouncements());
    }

    @GetMapping("/getAnnouncement/{eventAnnouncementId}")
    public ResponseEntity<ResponseDTO> getAnnouncement(@PathVariable int eventAnnouncementId){
        return ResponseEntity.ok().body(eventAnnouncementService.getEventAnnouncement(eventAnnouncementId));
    }

    @PostMapping(value ="/saveAnnouncement", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> saveAnnouncement(@RequestBody EventAnnouncementDTO eventAnnouncementDTO){
        //return ResponseEntity.ok(announcementService.saveAnnouncement(announcementDTO));
        return ResponseEntity.ok().body(eventAnnouncementService.saveEventAnnouncement(eventAnnouncementDTO));
    }

    @PutMapping(value ="/updateAnnouncement/{eventAnnouncementId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> updateAnnouncement(@PathVariable int eventAnnouncementId, @RequestBody EventAnnouncementDTO eventAnnouncementDTO){
        return ResponseEntity.ok().body(eventAnnouncementService.updateEventAnnouncement(eventAnnouncementId, eventAnnouncementDTO));
    }

    @DeleteMapping("/deleteAnnouncement/{eventAnnouncementId}")
    public ResponseEntity<ResponseDTO> deleteAnnouncement(@PathVariable int eventAnnouncementId){
        return ResponseEntity.ok().body(eventAnnouncementService.deleteEventAnnouncement(eventAnnouncementId));
    }


}
