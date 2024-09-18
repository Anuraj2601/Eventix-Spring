package com.example.eventix.service;

import com.example.eventix.entity.Candidate;
import com.example.eventix.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    public Candidate save(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Optional<Candidate> findById(Long id) {
        return candidateRepository.findById(id);
    }

    public List<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    public void deleteById(Long id) {
        candidateRepository.deleteById(id);
    }

    // Additional methods if necessary
}
