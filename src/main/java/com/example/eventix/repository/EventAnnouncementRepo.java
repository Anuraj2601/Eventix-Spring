package com.example.eventix.repository;

import com.example.eventix.entity.EventAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventAnnouncementRepo extends JpaRepository<EventAnnouncement, Integer> {
}
