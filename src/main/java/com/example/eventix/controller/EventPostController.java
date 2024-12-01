package com.example.eventix.controller;

import com.example.eventix.dto.EventPostDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.EventPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/member")
@CrossOrigin(origins = "https://eventix-18.netlify.app")
public class EventPostController {

    @Autowired
    private EventPostService eventPostService;

    @GetMapping("/getAllEventPosts")
    public ResponseEntity<ResponseDTO> getAllEventPosts() {
        return ResponseEntity.ok().body(eventPostService.getAllEventPosts());
    }

    @GetMapping("/getEventPost/{eventPostId}")
    public ResponseEntity<ResponseDTO> getEventPost(@PathVariable int eventPostId) {
        return ResponseEntity.ok().body(eventPostService.getEventPost(eventPostId));
    }

    @PostMapping(value="/saveEventPost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> saveEventPost(@RequestPart("data") EventPostDTO eventPostDTO, @RequestPart(value = "file", required = false) MultipartFile file ) {
        System.out.println("Published User ID in post controller: " + eventPostDTO);
        return ResponseEntity.ok().body(eventPostService.saveEventPost(eventPostDTO, file));
    }

    @PutMapping(value="/updateEventPost/{eventPostId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> updateEventPost(@PathVariable int eventPostId, @RequestPart("data") EventPostDTO eventPostDTO, @RequestPart(value = "file", required = false) MultipartFile file ) {
        return ResponseEntity.ok().body(eventPostService.updateEventPost(eventPostId, eventPostDTO, file));
    }

    @DeleteMapping("/deleteEventPost/{eventPostId}")
    public ResponseEntity<ResponseDTO> deleteEventPost(@PathVariable int eventPostId) {
        return ResponseEntity.ok().body(eventPostService.deleteEventPost(eventPostId));
    }

}
