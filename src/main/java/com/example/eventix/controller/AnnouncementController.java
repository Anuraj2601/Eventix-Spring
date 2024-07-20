package com.example.eventix.controller;

import com.example.eventix.dto.AnnouncementDTO;
import com.example.eventix.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/announcement")
@CrossOrigin
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/getAnnouncements")
    public String getAllAnnouncements() {
        return "All announcements";
    }

    @GetMapping("/Announcement/{announcement_id}")
    public String getAnnouncement(){
        return "1 announcements";
    }

    @PostMapping("/saveAnnouncement")
    public ResponseEntity<AnnouncementDTO> saveAnnouncement(@RequestBody AnnouncementDTO announcementDTO){
        //return ResponseEntity.ok(announcementService.saveAnnouncement(announcementDTO));
        return ResponseEntity.ok().body(announcementService.saveAnnouncement(announcementDTO));
    }

    @PutMapping("/updateAnnouncement")
    public String updateAnnouncement(){
        return "update announcements";
    }

    @DeleteMapping("/deleteAnnouncement")
    public String deleteAnnouncement(){
        return "delete announcements";
    }

}
