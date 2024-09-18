package com.example.eventix.repository;

import com.example.eventix.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    // Custom query methods if needed
}
