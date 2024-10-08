package com.example.eventix.dto;

import com.example.eventix.entity.Event_Sponsor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Event_SponsorDTO {
    private int sponsor_id;
    private String sponsor_description;
    private String sponsor_name;
    private String company_logo;
    private Event_Sponsor.SponsorType sponsorType;
    private String contact_person;
    private String contact_email;
    private int amount;

}
