package com.example.eventix.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SendQrCodeRequest {
    private String email; // User's email
    private String qrCodeData; // QR Code Data (the data passed from the frontend)
    private String meetingName; // Name of the meeting
}
