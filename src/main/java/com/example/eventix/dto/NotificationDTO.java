package com.example.eventix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private int notification_id;
    private String notification;
    private boolean is_read;
    private Integer user_id;
    private int club_id;
    private LocalDateTime date_posted;

}
