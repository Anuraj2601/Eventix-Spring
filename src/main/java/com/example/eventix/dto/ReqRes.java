package com.example.eventix.dto;

import com.example.eventix.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private Integer id;
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String regNo;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String role;
    private boolean active;
    private String otp;
    private LocalDateTime otpGeneratedTime;
//    private String imageFileName;
//    private String imageContentType;
    private Users users;
    private List<Users> usersList;
}
