package com.example.eventix.repository;

import com.example.eventix.entity.EventPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventPostRepo extends JpaRepository<EventPost, Integer> {
}
