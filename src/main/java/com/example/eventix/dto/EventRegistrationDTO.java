package com.example.eventix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRegistrationDTO {

    private int event_reg_id;
    private String reason;
    private String mobile_no;
    private boolean is_checked;
    private Integer user_id;
    private int event_id;
    private int club_id;
}
