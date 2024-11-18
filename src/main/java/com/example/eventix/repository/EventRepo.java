package com.example.eventix.repository;

import com.example.eventix.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface EventRepo extends JpaRepository<Event,Integer> {
    @Query("SELECT e FROM Event e WHERE e.club.club_id = :clubId")
    List<Event> findEventsByClubId(@Param("clubId") int clubId);

    @Query("SELECT e FROM Event e JOIN FETCH e.club")
    List<Event> findAllEventsWithClubDetails();
}
