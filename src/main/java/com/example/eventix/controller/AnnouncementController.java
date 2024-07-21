package com.example.eventix.controller;

import com.example.eventix.dto.AnnouncementDTO;
import com.example.eventix.dto.ResponseDTO;
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
    public ResponseEntity<ResponseDTO> getAllAnnouncements() {
        return ResponseEntity.ok().body(announcementService.getAllAnnouncements());
    }

    @GetMapping("/getAnnouncement/{announcement_id}")
    public ResponseEntity<ResponseDTO> getAnnouncement(@PathVariable int announcement_id){
        return ResponseEntity.ok().body(announcementService.getAnnouncement(announcement_id));
    }

    @PostMapping("/saveAnnouncement")
    public ResponseEntity<ResponseDTO> saveAnnouncement(@RequestBody AnnouncementDTO announcementDTO){
        //return ResponseEntity.ok(announcementService.saveAnnouncement(announcementDTO));
        return ResponseEntity.ok().body(announcementService.saveAnnouncement(announcementDTO));
    }

    @PutMapping("/updateAnnouncement")
    public ResponseEntity<ResponseDTO> updateAnnouncement(@RequestBody AnnouncementDTO announcementDTO){
        return ResponseEntity.ok().body(announcementService.updateAnnouncement(announcementDTO));
    }

    @DeleteMapping("/deleteAnnouncement")
    public ResponseEntity<ResponseDTO> deleteAnnouncement(@RequestBody AnnouncementDTO announcementDTO){
        return ResponseEntity.ok().body(announcementService.deleteAnnouncement(announcementDTO));
    }

}
