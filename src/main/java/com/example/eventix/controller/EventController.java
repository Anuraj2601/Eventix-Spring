package com.example.eventix.controller;

import com.example.eventix.constant.Constant;
import com.example.eventix.dto.EventDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Event;
import com.example.eventix.service.EventService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/event/")

@CrossOrigin("https://eventix-18.netlify.app")

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

    @PutMapping("/updateBudgetStatus/{eventId}")
    public ResponseEntity<ResponseDTO> updateBudgetStatus(@PathVariable int eventId, @RequestParam("status") int status, @RequestParam("role") String role) {

        // Ensure only the treasurer can perform this operation
        if (!"treasurer".equalsIgnoreCase(role)) {
            ResponseDTO responseDTO = new ResponseDTO();
            //responseDTO.setStatusCode(VarList.RSP_UNAUTHORIZED);
            responseDTO.setMessage("Only the treasurer can update the budget status.");
            return ResponseEntity.status(403).body(responseDTO);
        }

        // Call service method to update the budget status
        return ResponseEntity.ok().body(eventService.updateBudgetStatus(eventId, status));
    }


    @PutMapping("/updateIudStatus/{eventId}")
    public ResponseEntity<ResponseDTO> updateIudStatus(@PathVariable int eventId, @RequestParam("status") int status, @RequestParam("role") String role) {

        // Ensure only the treasurer can perform this operation
        if (!"admin".equalsIgnoreCase(role)) {
            ResponseDTO responseDTO = new ResponseDTO();
            //responseDTO.setStatusCode(VarList.RSP_UNAUTHORIZED);
            responseDTO.setMessage("Only the Admin can update the budget status.");
            return ResponseEntity.status(403).body(responseDTO);
        }

        // Call service method to update the budget status
        return ResponseEntity.ok().body(eventService.updateIudStatus(eventId, status));
    }

    @GetMapping("/getEvent/{event_id}")
    public ResponseEntity<ResponseDTO> getEvent(@PathVariable int event_id){
        return ResponseEntity.ok().body(eventService.getEvent(event_id));
    }

// New endpoint to serve event images
//@GetMapping("/image/{imageName}")
//public ResponseEntity<Resource> getEventImage(@PathVariable String imageName) {
//    try {
//        // Path to the directory where images are stored
//        Path imagePath = Paths.get("src/main/resources/static/uploads/posts/").resolve(imageName).normalize();
//        UrlResource resource = new UrlResource(imagePath.toUri());
//        if (resource.exists()) {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_JPEG) // Adjust content type based on your image format (e.g., IMAGE_PNG)
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageName + "\"")
//                    .body((Resource) resource);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    } catch (MalformedURLException e) {
//        return ResponseEntity.badRequest().build();
//    }
//}


@GetMapping("/uploads/events/eventImages/{imageName}")
public ResponseEntity<Resource> getEventImage(@PathVariable String imageName) {
    try {
        // Using the constant for the event photo directory
        Path imagePath = Paths.get(Constant.EVENT_PHOTO_DIRECTORY).resolve(imageName).normalize();
        UrlResource resource = new UrlResource(imagePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            // Detect content type
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageName + "\"")
                    .body((Resource) resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    } catch (MalformedURLException e) {
        return ResponseEntity.badRequest().build();
    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

    @PostMapping(value = "/saveEvent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> saveEvent(@RequestPart("data") EventDTO eventDTO, @RequestPart(value = "eventImage", required = false) MultipartFile eventImage, @RequestPart(value = "budgetFile", required = true) MultipartFile budgetFile) {
        return ResponseEntity.ok().body(eventService.saveEvent(eventDTO,eventImage,budgetFile));
    }

    @DeleteMapping("/deleteEvent/{event_id}")
    public ResponseEntity<ResponseDTO> deleteEventById(@PathVariable int event_id){
        return ResponseEntity.ok().body(eventService.deleteEventById(event_id));
    }

    @PutMapping(value = "/updateEvent/{event_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> updateEvent(
            @PathVariable int event_id,
            @RequestPart("data") EventDTO eventDTO,
            @RequestPart(value = "eventImage", required = false) MultipartFile eventImage,
            @RequestPart(value = "budgetFile", required = false) MultipartFile budgetFile) {
        return ResponseEntity.ok().body(eventService.updateEventById(event_id, eventDTO, eventImage, budgetFile));
    }

    @GetMapping("/{id}/download-proposal")
    public ResponseEntity<byte[]> downloadEventProposal(@PathVariable int id) {
        try {
            // Call the service method to generate the PDF
            byte[] pdfBytes = eventService.generateEventProposal(id);

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "event_proposal.pdf");

            // Return the PDF as a ResponseEntity
            return ResponseEntity.ok().headers(headers).body(pdfBytes);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}
