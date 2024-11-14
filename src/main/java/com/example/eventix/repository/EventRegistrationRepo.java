package com.example.eventix.repository;

import com.example.eventix.entity.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRegistrationRepo extends JpaRepository<EventRegistration, Integer> {
}
