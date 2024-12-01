package com.example.eventix.controller;

import com.example.eventix.dto.CandidateDTO;
import com.example.eventix.entity.Candidate;
import com.example.eventix.entity.Election;
import com.example.eventix.service.CandidateService;
import com.example.eventix.service.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "https://eventix-18.netlify.app")
@RestController
@RequestMapping("/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private ElectionService electionService;

    @PostMapping
    public ResponseEntity<CandidateDTO> createCandidate(@RequestBody CandidateDTO candidateDTO) {
        Optional<Election> electionOptional = electionService.findById(candidateDTO.getElectionId());
        if (electionOptional.isPresent()) {
            Election election = electionOptional.get();
            Candidate candidate = new Candidate();
            candidate.setContribution(candidateDTO.getContribution());
            candidate.setPosition(candidateDTO.getPosition());
            candidate.setUserEmail(candidateDTO.getUserEmail());
            candidate.setElection(election);
            candidate.setClubId(election.getClub().getClub_id()); // Ensure the club_id is set correctly
            candidate.setSelected("applied");
            candidate.setVotes(0);

            Candidate savedCandidate = candidateService.save(candidate);
            candidateDTO.setId(savedCandidate.getId());
            candidateDTO.setClubId(savedCandidate.getClubId());
            candidateDTO.setElectionId(savedCandidate.getElection().getElection_id());
            return ResponseEntity.status(HttpStatus.CREATED).body(candidateDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateDTO> updateCandidate(@PathVariable Long id, @RequestBody CandidateDTO candidateDTO) {
        Optional<Candidate> candidateOptional = candidateService.findById(id);
        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();

            // Update fields that are provided
            if (candidateDTO.getContribution() != null) {
                candidate.setContribution(candidateDTO.getContribution());
            }
            if (candidateDTO.getPosition() != null) {
                candidate.setPosition(candidateDTO.getPosition());
            }
            if (candidateDTO.getUserEmail() != null) {
                candidate.setUserEmail(candidateDTO.getUserEmail());
            }
            if (candidateDTO.getSelected() != null) {
                candidate.setSelected(candidateDTO.getSelected());
                // Avoid updating votes if only selected is modified
                if (candidateDTO.getVotes() != null) {
                    // You may include validation here if needed
                    candidate.setVotes(candidateDTO.getVotes());
                }
            } else {
                // Update the votes field only if a new value is provided and not null
                if (candidateDTO.getVotes() != null) {
                    candidate.setVotes(candidateDTO.getVotes());
                }
            }

            // Save the updated candidate
            Candidate updatedCandidate = candidateService.save(candidate);

            // Convert the updated candidate to DTO
            CandidateDTO updatedCandidateDTO = new CandidateDTO();
            updatedCandidateDTO.setId(updatedCandidate.getId());
            updatedCandidateDTO.setContribution(updatedCandidate.getContribution());
            updatedCandidateDTO.setPosition(updatedCandidate.getPosition());
            updatedCandidateDTO.setUserEmail(updatedCandidate.getUserEmail());
            updatedCandidateDTO.setSelected(updatedCandidate.getSelected());
            updatedCandidateDTO.setVotes(updatedCandidate.getVotes());

            // Return the updated candidate DTO
            return ResponseEntity.ok(updatedCandidateDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable Long id) {
        Optional<Candidate> candidateOptional = candidateService.findById(id);
        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            CandidateDTO candidateDTO = new CandidateDTO();
            candidateDTO.setId(candidate.getId());
            candidateDTO.setContribution(candidate.getContribution());
            candidateDTO.setPosition(candidate.getPosition());
            candidateDTO.setUserEmail(candidate.getUserEmail());
            candidateDTO.setElectionId(candidate.getElection().getElection_id());
            candidateDTO.setClubId(candidate.getClubId());
            candidateDTO.setSelected(candidate.getSelected());
            candidateDTO.setVotes(candidate.getVotes());
            return ResponseEntity.ok(candidateDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<CandidateDTO>> getAllCandidates() {
        List<Candidate> candidates = candidateService.findAll();
        List<CandidateDTO> candidateDTOs = candidates.stream().map(candidate -> {
            CandidateDTO candidateDTO = new CandidateDTO();
            candidateDTO.setId(candidate.getId());
            candidateDTO.setContribution(candidate.getContribution());
            candidateDTO.setPosition(candidate.getPosition());
            candidateDTO.setUserEmail(candidate.getUserEmail());
            candidateDTO.setElectionId(candidate.getElection().getElection_id()); // Correct field
            candidateDTO.setClubId(candidate.getClubId());
            candidateDTO.setSelected(candidate.getSelected());
            candidateDTO.setVotes(candidate.getVotes());
            candidateDTO.setName(candidate.getName()); // Ensure this is populated
            candidateDTO.setImageUrl(candidate.getImageUrl()); // Ensure this is populated
            candidateDTO.setOc(candidate.getOc()); // Fetch and set the oc column value
            candidateDTO.setPerformance(candidate.getPerformance()); // Fetch and set the performance column value
            return candidateDTO;
        }).toList();
        return ResponseEntity.ok(candidateDTOs);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        try {
            candidateService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PatchMapping("/{id}")
    public ResponseEntity<CandidateDTO> updateCandidateSelection(
            @PathVariable Long id,
            @RequestParam String selected) {
        CandidateDTO updatedCandidate = candidateService.updateCandidateSelection(id, selected);
        return ResponseEntity.ok(updatedCandidate);
    }

    @PatchMapping("/vote")
    public ResponseEntity<Void> incrementVotes(@RequestBody List<Long> candidateIds) {
        candidateService.incrementVotes(candidateIds);
        return ResponseEntity.ok().build();
    }




}
