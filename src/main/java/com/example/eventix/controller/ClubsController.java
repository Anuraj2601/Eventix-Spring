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
    private ClubsService clubsService;

    @GetMapping("/getAllClubs")
    public ResponseEntity<ResponseDTO> getAllClubs() {
        return ResponseEntity.ok().body(clubsService.getAllClubs());
    }

    @GetMapping("/getClub/{club_id}")
    public ResponseEntity<ResponseDTO> getClubById(@PathVariable("club_id") Integer clubId) {
        return ResponseEntity.ok().body(clubsService.getClub(clubId));
    }

    @PostMapping(value = "/addClub", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> addClub(@RequestPart("data") ClubsDTO clubsDTO,
                                               @RequestPart(value = "file", required = false) MultipartFile file) {
        System.out.println("club DTO: " + clubsDTO);
        return ResponseEntity.ok().body(clubsService.saveClub(clubsDTO, file));
    }

    @PutMapping(value = "/updateClub/{club_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> updateClub(@PathVariable("club_id") Integer clubId,
                                                  @RequestPart("data") ClubsDTO clubsDTO,
                                                  @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok().body(clubsService.updateClub(clubId, clubsDTO, file));
    }

    @DeleteMapping("/deleteClub/{club_id}")
    public ResponseEntity<ResponseDTO> deleteClub(@PathVariable("club_id") Integer clubId) {
        return ResponseEntity.ok().body(clubsService.deleteClub(clubId));
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<ResponseDTO> updateClubState(@PathVariable("id") Integer clubId, @RequestBody ClubsDTO updateDTO) {
        ResponseDTO response = clubsService.updateClubState(clubId, updateDTO.isState());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @GetMapping("/details/{id}")
    public ResponseEntity<ResponseDTO> getClubDetails(@PathVariable("id") Integer clubId) {
        return ResponseEntity.ok().body(clubsService.getClubById(clubId));
    }
}
