package com.example.eventix.controller;

import com.example.eventix.dto.VoterDTO;
import com.example.eventix.entity.Voter;
import com.example.eventix.service.VoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/voters")
public class VoterController {

    @Autowired
    private VoterService voterService;

    @PostMapping
    public ResponseEntity<Voter> addVoter(@RequestBody VoterDTO voterDTO) {
        return ResponseEntity.ok(voterService.addVoter(voterDTO));
    }

    @GetMapping
    public ResponseEntity<List<VoterDTO>> getAllVoters() {
        return ResponseEntity.ok(voterService.getAllVoters());
    }

    @GetMapping("/election/{electionId}")
    public ResponseEntity<Boolean> getVotersByElection(@PathVariable Long electionId, @RequestParam Long userId) {
        // Fetch voters by electionId
        List<VoterDTO> voters = voterService.getVotersByElection(electionId);

        // Check if the user has voted in the election
        boolean userVoted = voters.stream()
                .anyMatch(voter -> voter.getUserId().equals(userId) && voter.isVoted());

        // Return boolean (true if the user has voted, false otherwise)
        return ResponseEntity.ok(userVoted);
    }



}
