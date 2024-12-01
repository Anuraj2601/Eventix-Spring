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

import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Users;
import com.example.eventix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.eventix.dto.UserProfileDTO;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "https://eventix-18.netlify.app")
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profiles")
    public List<UserProfileDTO> getAllUserProfiles() {
        return userService.getAllUserProfiles();
    }

    @GetMapping("/profile")
    public UserProfileDTO getUserProfileByEmail(@RequestParam String email) {
        return userService.getUserProfileByEmail(email);
    }

//    @GetMapping
//    public List<Users> getAllUsers() {
//
//        return userService.getAllUsers();
//    }

    @GetMapping("/getAllUsersIncludingCurrent")
    public List<Users> getAllUsers() {
        // Retrieve the currently logged-in user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Assumes email is used as the username

        // Fetch all users and include the current user with a flag
        return userService.getAllUsers()
                .stream()
                .peek(user -> {
                    if (user.getEmail().equals(currentUserEmail)) {
                        user.setBio(user.getBio() + " (Current User)"); // Example of marking the current user
                    }
                })
                .collect(Collectors.toList());
    }


    @GetMapping
    public List<Users> getAllUsersIncludingCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Assumes email is used as the username

        // Fetch all users and filter out the current user
        return userService.getAllUsers()
                .stream()
                .filter(user -> !user.getEmail().equals(currentUserEmail)) // Filter out the current user
                .collect(Collectors.toList());
    }



    @GetMapping("/getUserByEmail")
    public ResponseEntity<ResponseDTO> getUserByEmail(@RequestParam String email){
        //return userService.getUserByEmail(email);
        return ResponseEntity.ok().body(userService.getUserByEmail(email));

    }
}
