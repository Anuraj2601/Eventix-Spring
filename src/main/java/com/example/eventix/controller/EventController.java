package com.example.eventix.controller;

import com.example.eventix.entity.Event;
import com.example.eventix.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/event")
@CrossOrigin
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/getAllEvents")
    public String getAllEvents() {
        return "ALL events";
    }

    @GetMapping("/getEvent/{event_id}")
    public String getEvent(){
        return "1 event";
    }

    @PostMapping("/saveEvent")
    public String saveEvent(){
        return "save event";
    }

    @PutMapping("/updateEvent")
    public String updateEvent(){
        return "update event";
    }

    @DeleteMapping("/deleteEvent/{event_id}")
    public String deleteEvent(){
        return "delete event";
    }

}
