package com.example.eventix.service;
import com.example.eventix.dto.CandidateDTO;

import com.example.eventix.entity.Candidate;
import com.example.eventix.entity.Users;
import com.example.eventix.repository.CandidateRepository;
import com.example.eventix.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UsersRepo usersRepo;

    // Save a candidate with populated user details
    public Candidate save(Candidate candidate) {
        populateUserDetails(candidate);
        return candidateRepository.save(candidate);
    }

    // Find candidate by ID
    public Optional<Candidate> findById(Long id) {
        return candidateRepository.findById(id);
    }

    // Find all candidates
    public List<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    // Delete candidate by ID
    public void deleteById(Long id) {
        candidateRepository.deleteById(id);
    }

    // Populate user details based on email
    private void populateUserDetails(Candidate candidate) {
        Optional<Users> userOpt = usersRepo.findByEmail(candidate.getUserEmail());
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
            candidate.setName(user.getFirstname() + " " + user.getLastname());
            candidate.setImageUrl(user.getPhotoUrl());
        }
    }

    public CandidateDTO updateCandidateSelection(Long id, String selected) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));
        candidate.setSelected(selected);
        candidate = candidateRepository.save(candidate);
        return mapToDTO(candidate);
    }
    private CandidateDTO mapToDTO(Candidate candidate) {
        Integer electionId = null;
        if (candidate.getElection() != null) {
            electionId = candidate.getElection().getElection_id(); // Ensure getter exists
        }

        return new CandidateDTO(
                candidate.getId(),
                candidate.getContribution(),
                candidate.getPosition(),
                candidate.getUserEmail(),
                electionId, // Pass the Integer electionId
                candidate.getClubId(),
                candidate.getSelected(),
                candidate.getVotes(),
                candidate.getName(),
                candidate.getImageUrl(),
                candidate.getOc(),           // Pass the JSON data for event names
                candidate.getPerformance()   // Pass the performance percentage or integer
        );
    }
    public void incrementVotes(List<Long> candidateIds) {
        List<Candidate> candidates = candidateRepository.findAllById(candidateIds);
        for (Candidate candidate : candidates) {
            candidate.setVotes(candidate.getVotes() + 1);
        }
        candidateRepository.saveAll(candidates);
    }


}
