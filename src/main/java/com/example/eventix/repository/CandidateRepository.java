package com.example.eventix.repository;

import com.example.eventix.dto.WinningCandidateDTO;
import com.example.eventix.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    @Query("SELECT new com.example.eventix.dto.WinningCandidateDTO(c.userId, c.position, c.clubId) " +
            "FROM Candidate c " +
            "WHERE c.election.id = :electionId AND c.isWinning = true") // Assuming there's an isWinning field
    List<WinningCandidateDTO> findWinningCandidates(@Param("electionId") int electionId);
}
