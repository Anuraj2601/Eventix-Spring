package com.example.eventix.controller;

import com.example.eventix.dto.ClubsDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.ClubsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/clubs")
@CrossOrigin("http://localhost:5173")
public class ClubsController {
    @Autowired
    ClubsService clubsService;

    @GetMapping("/getAllClubs")
    public ResponseEntity<ResponseDTO> getAllClubs() {
        return ResponseEntity.ok().body(clubsService.getAllClubs());
    }

    @GetMapping("/getClub/{club_id}")
    public ResponseEntity<ResponseDTO> getClubById(@PathVariable Integer club_id) {
        return ResponseEntity.ok().body(clubsService.getClub(club_id));
    }

    @PostMapping(value = "/addClub", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> addClubs(@RequestPart("data")  ClubsDTO clubsDTO, @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok().body(clubsService.saveClub(clubsDTO,file));
    }

    @PutMapping(value = "/updateClub/{club_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> updateClub(@PathVariable Integer club_id, @RequestPart("data") ClubsDTO clubsDTO, @RequestPart(value = "file",required = false) MultipartFile file) {
        return ResponseEntity.ok().body(clubsService.updateClub(club_id,clubsDTO,file));
    }

    @DeleteMapping("/deleteClub/{club_id}")
    public ResponseEntity<ResponseDTO> deleteClub(@PathVariable Integer club_id) {
        return ResponseEntity.ok().body(clubsService.deleteClub(club_id));
    }
}
