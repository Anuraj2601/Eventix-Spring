package com.example.eventix.controller;

import com.example.eventix.dto.EventDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Event;
import com.example.eventix.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/event/")

@CrossOrigin("http://localhost:5173")

public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/getAllEvents")
    public ResponseEntity<ResponseDTO> getAllEvents() {
        return ResponseEntity.ok().body(eventService.getAllEvents());
    }

    @GetMapping("/getAllEventsWithClubs")
    public ResponseEntity<ResponseDTO> getAllEventsWithClubs() {
        return ResponseEntity.ok().body(eventService.getAllEventsWithClubDetails());
    }

    @GetMapping("/getEventsByClub/{clubId}")
    public ResponseEntity<ResponseDTO> getEventsByClubId(@PathVariable int clubId) {
        return ResponseEntity.ok().body(eventService.getEventsByClubId(clubId));
    }

//    @GetMapping("/getEvent/{event_id}")
//    public ResponseEntity<ResponseDTO> getEvent(@PathVariable int event_id){
//        return ResponseEntity.ok().body(eventService.getEvent(event_id));
//    }

    @PostMapping(value = "/saveEvent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> saveEvent(@RequestPart("data") EventDTO eventDTO, @RequestPart(value = "eventImage", required = false) MultipartFile eventImage, @RequestPart(value = "budgetFile", required = true) MultipartFile budgetFile) {
        return ResponseEntity.ok().body(eventService.saveEvent(eventDTO,eventImage,budgetFile));
    }

//    @PutMapping(value = "/updateEvent/{event_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ResponseDTO> updateEvent(@PathVariable int event_id, @RequestPart("data") EventDTO eventDTO, @RequestPart(value = "file", required = false) MultipartFile file){
//        return ResponseEntity.ok().body(eventService.updateEvent(event_id, eventDTO, file));
//    }
//
//    @DeleteMapping("/deleteEvent/{event_id}")
//    public ResponseEntity<ResponseDTO> deleteEvent(@PathVariable int event_id){
//        return ResponseEntity.ok().body(eventService.deleteEvent(event_id));
//    }

}
