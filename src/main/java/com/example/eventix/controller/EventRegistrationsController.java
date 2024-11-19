package com.example.eventix.controller;


import com.example.eventix.dto.EventOcDTO;
import com.example.eventix.dto.EventRegistrationDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.EventRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "http://localhost:5173")
public class EventRegistrationsController {

    @Autowired
    private EventRegistrationService eventRegistrationService;

    @GetMapping("/getAllEventRegistrations")
    public ResponseEntity<ResponseDTO> getAllEventRegistrations() {
        return ResponseEntity.ok().body(eventRegistrationService.getAllEventRegistrations());
    }

    @GetMapping("/getEventRegistration/{eventRegId}")
    public ResponseEntity<ResponseDTO> getEventRegistration(@PathVariable int eventRegId){
        return ResponseEntity.ok().body(eventRegistrationService.getEventRegistration(eventRegId));
    }

    @PostMapping(value ="/saveEventRegistration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> saveEventRegistration(@RequestBody EventRegistrationDTO eventRegistrationDTO){
        //return ResponseEntity.ok(announcementService.saveAnnouncement(announcementDTO));
        return ResponseEntity.ok().body(eventRegistrationService.saveEventRegistration(eventRegistrationDTO));
    }

    @PutMapping(value ="/updateEventRegistration/{eventRegId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> updateEventRegistration(@PathVariable int eventRegId, @RequestBody EventRegistrationDTO eventRegistrationDTO){
        return ResponseEntity.ok().body(eventRegistrationService.updateEventRegistration(eventRegId, eventRegistrationDTO));
    }

    @DeleteMapping("/deleteEventRegistration/{eventRegId}")
    public ResponseEntity<ResponseDTO> deleteEventRegistration(@PathVariable int eventRegId){
        return ResponseEntity.ok().body(eventRegistrationService.deleteEventRegistration(eventRegId));
    }

    @PutMapping(value = "/registrationCheckIn/{eventRegId}")
    public ResponseEntity<ResponseDTO> registrationCheckIn(@PathVariable int eventRegId){
        return ResponseEntity.ok().body(eventRegistrationService.registrationCheckIn(eventRegId));
    }





}
