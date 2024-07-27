package com.example.eventix.controller;

import com.example.eventix.dto.Event_SponsorDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.Event_SponsorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/president")
public class Event_SponsorController {
    @Autowired
    private Event_SponsorService eventSponsorService;

    @GetMapping("/getAllSponser")
    public ResponseEntity<ResponseDTO> getAllSponsor() {
        return ResponseEntity.ok().body(eventSponsorService.getAllSponsors());
    }

    @GetMapping("/getSponsor/{sponsor_id}")
    public ResponseEntity<ResponseDTO> getSponsorById(@PathVariable Integer sponsor_id) {
        return ResponseEntity.ok().body(eventSponsorService.getSponsor(sponsor_id));
    }

    @PostMapping(value = "/addSponsor", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> addSponsor(@RequestPart("data") Event_SponsorDTO event_sponsorDTO,@RequestPart(value = "file", required = false) MultipartFile file ) {

        return ResponseEntity.ok().body(eventSponsorService.saveSponsor(event_sponsorDTO,file));
    }

    @PutMapping(value = "/updateSponsor/{sponsor_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> updateSponsor(@PathVariable Integer sponsor_id ,@RequestPart("data") Event_SponsorDTO eventSponsorDTO,@RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok().body(eventSponsorService.updateSponsor(sponsor_id ,eventSponsorDTO,file));
    }

    @DeleteMapping("/deleteSponsor/{sponsor_id}")
    public ResponseEntity<ResponseDTO> deleteSponsor(@PathVariable Integer sponsor_id) {
        return ResponseEntity.ok().body(eventSponsorService.deleteSponsor(sponsor_id));
    }

}
