package com.example.eventix.repository;

import com.example.eventix.entity.EventOc;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventOcRepo extends JpaRepository<EventOc,Integer> {
    //Optional<EventOc> findByMemberIdAndEventId(int userId, int eventId);

}
