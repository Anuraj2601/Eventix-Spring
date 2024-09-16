//package com.example.eventix.controller;
//
//import com.example.eventix.entity.Users;
//import com.example.eventix.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:5173")
//@RequestMapping("/api/users")
//public class UserController {
//    @Autowired
//    private UserService userService;
//
//    @GetMapping
//    public List<Users> getAllUsers() {
//        // This service returns a list of UserProfileDto which contains image URL and name.
//        return userService.getAllUsers();
//    }
//
//
//}
package com.example.eventix.controller;

import com.example.eventix.entity.Users;
import com.example.eventix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<Users> getAllUsers() {
        // Retrieve the currently logged-in user's email (or username)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Assumes email is used as the username

        // Fetch all users and filter out the current user
        return userService.getAllUsers()
                .stream()
                .filter(user -> !user.getEmail().equals(currentUserEmail)) // Filter out the current user
                .collect(Collectors.toList());
    }
}
