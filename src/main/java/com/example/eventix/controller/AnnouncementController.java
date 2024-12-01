package com.example.eventix.controller;

import com.example.eventix.dto.AnnouncementDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/president")
@CrossOrigin(origins = "https://eventix-18.netlify.app")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/getAllAnnouncements")
    public ResponseEntity<ResponseDTO> getAllAnnouncements() {
        return ResponseEntity.ok().body(announcementService.getAllAnnouncements());
    }

    @GetMapping("/getAnnouncement/{announcement_id}")
    public ResponseEntity<ResponseDTO> getAnnouncement(@PathVariable int announcement_id){
        return ResponseEntity.ok().body(announcementService.getAnnouncement(announcement_id));
    }

    @PostMapping(value ="/saveAnnouncement", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> saveAnnouncement(@RequestBody AnnouncementDTO announcementDTO){
        //return ResponseEntity.ok(announcementService.saveAnnouncement(announcementDTO));
        return ResponseEntity.ok().body(announcementService.saveAnnouncement(announcementDTO));
    }

    @PutMapping(value ="/updateAnnouncement/{announcement_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> updateAnnouncement(@PathVariable int announcement_id, @RequestBody AnnouncementDTO announcementDTO){
        return ResponseEntity.ok().body(announcementService.updateAnnouncement(announcement_id, announcementDTO));
    }

    @DeleteMapping("/deleteAnnouncement/{announcement_id}")
    public ResponseEntity<ResponseDTO> deleteAnnouncement(@PathVariable int announcement_id){
        return ResponseEntity.ok().body(announcementService.deleteAnnouncement(announcement_id));
    }

}
