package com.example.eventix.controller;

import com.example.eventix.dto.AnnouncementDTO;
import com.example.eventix.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/announcement")
@CrossOrigin
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/getAllAnnouncements")
    public ResponseEntity<List<AnnouncementDTO>> getAllAnnouncements() {
        return ResponseEntity.ok().body(announcementService.getAllAnnouncements());
    }

    @GetMapping("/getAnnouncement/{announcement_id}")
    public ResponseEntity<AnnouncementDTO> getAnnouncement(@PathVariable int announcement_id){
        return ResponseEntity.ok().body(announcementService.getAnnouncement(announcement_id));
    }

    @PostMapping("/saveAnnouncement")
    public ResponseEntity<AnnouncementDTO> saveAnnouncement(@RequestBody AnnouncementDTO announcementDTO){
        //return ResponseEntity.ok(announcementService.saveAnnouncement(announcementDTO));
        return ResponseEntity.ok().body(announcementService.saveAnnouncement(announcementDTO));
    }

    @PutMapping("/updateAnnouncement")
    public ResponseEntity<AnnouncementDTO> updateAnnouncement(@RequestBody AnnouncementDTO announcementDTO){
        return ResponseEntity.ok().body(announcementService.updateAnnouncement(announcementDTO));
    }

    @DeleteMapping("/deleteAnnouncement")
    public boolean deleteAnnouncement(@RequestBody AnnouncementDTO announcementDTO){
        return announcementService.deleteAnnouncement(announcementDTO);
    }

}
