package com.example.eventix.controller;

import com.example.eventix.dto.EventOcDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.EventOcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/president")
@CrossOrigin(origins = "https://eventix-18.netlify.app")
public class EventOcController {

    @Autowired
    private EventOcService eventOcService;

    @GetMapping("/getAllEventOcs")
    public ResponseEntity<ResponseDTO> getAllEventOcs() {
        return ResponseEntity.ok().body(eventOcService.getAllEventOcs());
    }

    @GetMapping("/getEventOc/{eventOc_id}")
    public ResponseEntity<ResponseDTO> getEventOc(@PathVariable int eventOc_id){
        return ResponseEntity.ok().body(eventOcService.getEventOc(eventOc_id));
    }

    @PostMapping(value ="/saveEventOc", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> saveEventOc(@RequestBody EventOcDTO eventOcDTO){
        //return ResponseEntity.ok(announcementService.saveAnnouncement(announcementDTO));
        return ResponseEntity.ok().body(eventOcService.saveEventOc(eventOcDTO));
    }

    @PutMapping(value ="/updateEventOc/{eventOc_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> updateEventOc(@PathVariable int eventOc_id, @RequestBody EventOcDTO eventOcDTO){
        return ResponseEntity.ok().body(eventOcService.updateEventOc(eventOc_id, eventOcDTO));
    }

    @DeleteMapping("/deleteEventOc/{eventOc_id}")
    public ResponseEntity<ResponseDTO> deleteEventOc(@PathVariable int eventOc_id){
        return ResponseEntity.ok().body(eventOcService.deleteEventOc(eventOc_id));
    }

    @PutMapping(value = "/removeEventOc/{eventOc_id}")
    public ResponseEntity<ResponseDTO> removeEventOc(@PathVariable int eventOc_id){
        return ResponseEntity.ok().body(eventOcService.removeEventOc(eventOc_id));
    }


}
