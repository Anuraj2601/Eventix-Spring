package com.example.eventix.controller;

import com.example.eventix.dto.RegistrationDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "http://localhost:5173")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/getAllRegistrations")
    public ResponseEntity<ResponseDTO> getAllRegistrations() {
        List<RegistrationDTO> registrations = registrationService.getAllRegistrations();
        ResponseDTO response = new ResponseDTO(200, "Successfully retrieved all registrations", registrations);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/getRegistration/{registration_id}")
    public ResponseEntity<ResponseDTO> getRegistration(@PathVariable int registration_id) {
        RegistrationDTO registration = registrationService.getRegistration(registration_id);
        ResponseDTO response = new ResponseDTO(200, "Successfully retrieved registration", registration);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/saveRegistration")
    public ResponseEntity<ResponseDTO> saveRegistration(@RequestBody RegistrationDTO registrationDTO) {
        RegistrationDTO savedRegistration = registrationService.saveRegistration(registrationDTO);
        ResponseDTO response = new ResponseDTO(201, "Registration successfully saved", savedRegistration);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value="/updateRegistration/{registration_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> updateRegistration(@PathVariable int registration_id, @RequestBody RegistrationDTO registrationDTO) {
        registrationDTO.setRegistration_id(registration_id); // Ensure the ID is set for update
        RegistrationDTO updatedRegistration = registrationService.saveRegistration(registrationDTO);
        ResponseDTO response = new ResponseDTO(200, "Registration successfully updated", updatedRegistration);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/deleteRegistration/{registration_id}")
    public ResponseEntity<ResponseDTO> deleteRegistration(@PathVariable int registration_id) {
        registrationService.deleteRegistration(registration_id);
        ResponseDTO response = new ResponseDTO(200, "Registration successfully deleted", null);
        return ResponseEntity.ok().body(response);
    }
}
