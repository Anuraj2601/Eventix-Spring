package com.example.eventix.controller;

import com.example.eventix.entity.Users;
import com.example.eventix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

//    @GetMapping
//    public List<Users> getAllUsers() {
//
//        return userService.getAllUsers();
//    }

    @GetMapping
    public List<Users> getAllUsers() {
        // This service returns a list of UserProfileDto which contains image URL and name.
        return userService.getAllUsers();
    }


}