package com.example.eventix.controller;

import com.example.eventix.dto.EventOcDTO;
import com.example.eventix.dto.NotificationDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/getAllNotifications")
    public ResponseEntity<ResponseDTO> getAllNotifications() {
        return ResponseEntity.ok().body(notificationService.getAllNotifications());
    }

    @GetMapping("/getNotification/{notification_id}")
    public ResponseEntity<ResponseDTO> getNotification(@PathVariable int notification_id){
        return ResponseEntity.ok().body(notificationService.getNotification(notification_id));
    }

    @GetMapping("/getNotificationsByUserId/{user_id}")
    public ResponseEntity<ResponseDTO> getNotificationsByUserId(@PathVariable int user_id){
        return ResponseEntity.ok().body(notificationService.getNotificationsByUserId(user_id));
    }

    @PostMapping(value ="/saveNotification", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> saveNotification(@RequestBody NotificationDTO notificationDTO){
        return ResponseEntity.ok().body(notificationService.saveNotification(notificationDTO));
    }
}
