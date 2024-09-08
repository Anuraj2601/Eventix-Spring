package com.example.eventix.repository;

import com.example.eventix.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Find messages sent to a specific receiver
    List<Message> findByReceiver(String receiver);

    // Find non-deleted messages
    List<Message> findByIsDeletedFalse();


}
