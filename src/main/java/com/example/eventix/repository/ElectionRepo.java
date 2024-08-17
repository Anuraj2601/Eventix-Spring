package com.example.eventix.repository;

import com.example.eventix.entity.Election;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectionRepo extends JpaRepository<Election, Integer> {
}
