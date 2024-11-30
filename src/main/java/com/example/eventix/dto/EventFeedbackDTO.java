package com.example.eventix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventFeedbackDTO {

    private int e_feedback_id;
    private String feedback;
    private Integer user_id;
    private int event_id;
    private int club_id;
    private LocalDateTime date_posted;

}
