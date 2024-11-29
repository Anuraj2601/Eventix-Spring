package com.example.eventix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QRCodeRequest {

        private String userId;
        private String meetingId;
        private String clubId;

        // Getters and setters
}
