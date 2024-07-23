package com.example.eventix.service;

import com.example.eventix.repository.EventRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepo eventRepo;
}
