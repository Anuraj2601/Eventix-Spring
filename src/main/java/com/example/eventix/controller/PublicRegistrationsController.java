package com.example.eventix.controller;

import com.example.eventix.dto.PublicRegistrationsDTO;
import com.example.eventix.service.PublicRegistrationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.eventix.entity.PublicRegistrations;


@RestController
@RequestMapping("/api/public-registrations")
public class PublicRegistrationsController {

    @Autowired
    private PublicRegistrationsService publicRegistrationsService;


    // Get all public registrations
    @GetMapping
    public ResponseEntity<Object> getAllPublicRegistrations() {
        return ResponseEntity.ok(publicRegistrationsService.getAllPublicRegistrations());
    }

    // Get public registrations by event
    @GetMapping("/event/{eventId}")
    public ResponseEntity<Object> getPublicRegistrationsByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(publicRegistrationsService.getPublicRegistrationsByEvent(eventId));
    }

    // Save new public registration
    @PostMapping
    public ResponseEntity<Object> savePublicRegistration(@RequestBody PublicRegistrationsDTO publicRegistrationsDTO) {
        System.out.println("Received registration data: " + publicRegistrationsDTO);
        PublicRegistrations savedRegistration = publicRegistrationsService.savePublicRegistration(publicRegistrationsDTO);
        return ResponseEntity.ok(savedRegistration);
    }

    // Update check-in status for a registration (PATCH)
    @PatchMapping("/{registrationId}/checkin")
    public Object updateCheckInStatus(@PathVariable Long registrationId) {
        // Directly set checkInStatus to 1 (indicating checked-in)
        int checkInStatus = 1;

        // Call the service method with the registrationId and checkInStatus
        return publicRegistrationsService.updateCheckInStatus(registrationId, checkInStatus);
    }

}
