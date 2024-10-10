package com.example.eventix.repository;

import com.example.eventix.dto.WinningCandidateDTO;
import com.example.eventix.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {


}
