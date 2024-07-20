package com.example.eventix.repository;

import com.example.eventix.entity.Announcements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepo extends JpaRepository<Announcements,Integer> {

}
