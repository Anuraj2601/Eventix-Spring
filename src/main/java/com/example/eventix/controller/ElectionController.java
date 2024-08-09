package com.example.eventix.controller;

import com.example.eventix.dto.ElectionDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Election;
import com.example.eventix.service.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/president")
@CrossOrigin(origins = "http://localhost:5173")
public class ElectionController {

    @Autowired
    private ElectionService electionService;

    @GetMapping("/getAllElections")
    public ResponseEntity<ResponseDTO> getAllElections() {
        return ResponseEntity.ok().body(electionService.getAllElections());
    }

    @GetMapping("/getElection/{election_id}")
    public ResponseEntity<ResponseDTO> getElection(@PathVariable int election_id) {
        return ResponseEntity.ok().body(electionService.getElection(election_id));
    }

    @PostMapping("/saveElection")
    public ResponseEntity<ResponseDTO> saveElection(@RequestBody ElectionDTO electionDTO) {
        return ResponseEntity.ok().body(electionService.saveElection(electionDTO));
    }

    @PutMapping("/updateElection/{election_id}")
    public ResponseEntity<ResponseDTO> updateElection(@PathVariable int election_id, @RequestBody ElectionDTO electionDTO) {
        return ResponseEntity.ok().body(electionService.updateElection(election_id, electionDTO));
    }

    @DeleteMapping("/deleteElection/{election_id}")
    public ResponseEntity<ResponseDTO> deleteElection(@PathVariable int election_id) {
        return ResponseEntity.ok().body(electionService.deleteElection(election_id));
    }


}
