package com.example.eventix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventOcDTO {

    private int oc_id;
    private String oc_name;
    private String team;
    private String event_name;
    private boolean is_removed;
    private int user_id;
    private int event_id;
}
