package com.example.eventix.repository;

import com.example.eventix.entity.EventFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventFeedbackRepo extends JpaRepository<EventFeedback, Integer> {
}
