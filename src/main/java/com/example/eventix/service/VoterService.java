package com.example.eventix.service;

import com.example.eventix.dto.VoterDTO;
import com.example.eventix.entity.Voter;
import com.example.eventix.repository.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoterService {

    @Autowired
    private VoterRepository voterRepository;

    public Voter addVoter(VoterDTO voterDTO) {
        Voter voter = new Voter();
        voter.setUserId(voterDTO.getUserId());
        voter.setElectionId(voterDTO.getElectionId());
        voter.setisVoted(voterDTO.isVoted() ? voterDTO.isVoted() : true); // Explicitly set true if not provided
        return voterRepository.save(voter);
    }


    public List<VoterDTO> getAllVoters() {
        return voterRepository.findAll().stream().map(voter ->
                new VoterDTO(voter.getUserId(), voter.getElectionId(), voter.isVoted())
        ).collect(Collectors.toList());
    }

    public List<VoterDTO> getVotersByElection(Long electionId) {
        return voterRepository.findByElectionId(electionId).stream().map(voter ->
                new VoterDTO(voter.getUserId(), voter.getElectionId(), voter.isVoted())
        ).collect(Collectors.toList());
    }

    public boolean hasUserVotedInElection(Long electionId, Long userId) {
        // Check if the user has voted for the specific election
        return voterRepository.existsByElectionIdAndUserIdAndIsVotedTrue(electionId, userId);
    }



}
