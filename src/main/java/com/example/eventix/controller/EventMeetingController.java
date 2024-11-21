package com.example.eventix.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/president")
@CrossOrigin(origins = "http://localhost:5173")
public class EventMeetingController {

}
