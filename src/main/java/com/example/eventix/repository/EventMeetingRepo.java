package com.example.eventix.repository;

import com.example.eventix.entity.EventMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventMeetingRepo extends JpaRepository<EventMeeting, Integer> {
}
