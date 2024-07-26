package com.example.eventix.controller;

import com.example.eventix.dto.EventDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Event;
import com.example.eventix.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/event")
@CrossOrigin
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/getAllEvents")
    public ResponseEntity<ResponseDTO> getAllEvents() {
        return ResponseEntity.ok().body(eventService.getAllEvents());
    }

    @GetMapping("/getEvent/{event_id}")
    public ResponseEntity<ResponseDTO> getEvent(@PathVariable int event_id){
        return ResponseEntity.ok().body(eventService.getEvent(event_id));
    }

    @PostMapping("/saveEvent")
    public ResponseEntity<ResponseDTO> saveEvent(@RequestBody EventDTO eventDTO){
        return ResponseEntity.ok().body(eventService.saveEvent(eventDTO));
    }

    @PutMapping("/updateEvent")
    public ResponseEntity<ResponseDTO> updateEvent(@RequestBody EventDTO eventDTO){
        return ResponseEntity.ok().body(eventService.updateEvent(eventDTO));
    }

    @DeleteMapping("/deleteEvent/{event_id}")
    public ResponseEntity<ResponseDTO> deleteEvent(@PathVariable int event_id){
        return ResponseEntity.ok().body(eventService.deleteEvent(event_id));
    }

}
