package com.example.eventix.repository;

import com.example.eventix.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepo extends JpaRepository<Meeting, Integer> {
}