package com.example.eventix.repository;

import com.example.eventix.entity.PublicRegistrations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicRegistrationsRepository extends JpaRepository<PublicRegistrations, Long> {
    List<PublicRegistrations> findByEventId(Long eventId);
}
