package com.example.eventix.repository;

import com.example.eventix.entity.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;


public interface ElectionRepo extends JpaRepository<Election, Integer> {
    @Procedure(procedureName = "ReleaseElectionResults")
    void callReleaseElectionResults(Integer electionId);
}
