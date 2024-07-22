package com.example.eventix.controller;

import com.example.eventix.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/meetings")
@CrossOrigin
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @GetMapping("/getAllMeetings")
    public String getAllMeetings() {
        return "All meetings";
    }

    @GetMapping("/getMeeting/{meeting_id}")
    public String getMeeting(){
        return "meeting";
    }

    @PostMapping("/saveMeeting")
    public String saveMeeting(){
        return "save Meeting";
    }

    @PutMapping("/updateMeeting")
    public String updateMeeting(){
        return "update Meeting";
    }

    @DeleteMapping("/deleteMeeting/{meeting_id}")
    public String deleteMeeting(){
        return "delete Meeting";
    }

}
