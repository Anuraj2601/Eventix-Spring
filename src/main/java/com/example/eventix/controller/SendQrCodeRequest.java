package com.example.eventix.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.Map;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SendQrCodeRequest {
    // Getters and setters
    private String email;
    private Map<Long, String> qrCodeDataUrls; // Map of participantId -> QR code URL

    public void setEmail(String email) {
        this.email = email;
    }

    public void setQrCodeDataUrls(Map<Long, String> qrCodeDataUrls) {
        this.qrCodeDataUrls = qrCodeDataUrls;
    }
}
