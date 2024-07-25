package com.example.eventix.repository;

import com.example.eventix.entity.Announcements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AnnouncementRepo extends JpaRepository<Announcements,Integer>, CrudRepository<Announcements,Integer> {

}
