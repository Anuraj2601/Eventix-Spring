package com.example.eventix.repository;

import com.example.eventix.entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoterRepository extends JpaRepository<Voter, Long> {
    List<Voter> findByElectionId(Long electionId);
    List<Voter> findByUserId(Long userId);
    boolean existsByElectionIdAndUserIdAndIsVotedTrue(Long electionId, Long userId);


}
