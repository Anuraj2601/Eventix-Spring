package com.example.eventix.controller;

import com.example.eventix.dto.ProfileDTO;
import com.example.eventix.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "https://eventix-18.netlify.app")
@RequestMapping("/api/user")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getProfile() {
        // Get the authenticated user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Assuming your UserDetails implementation (e.g., Users class) has the email or another identifier
        String email = userDetails.getUsername(); // Assuming username is the email

        // Retrieve the profile using the email
        ProfileDTO profile = profileService.getProfileByEmail(email);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profile);
    }


    // Endpoint to update the bio
    @PutMapping("/profile/bio")
    public ResponseEntity<Void> updateBio(@RequestBody String newBio) {
        // Get the authenticated user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername(); // Assuming username is the email

        // Update the bio in the service layer
        profileService.updateBio(email, newBio);

        // Return a success response
        return ResponseEntity.ok().build();
    }

    @PostMapping("/profile/photo")
    public ResponseEntity<?> updateProfilePhoto(@RequestParam("profileImage") MultipartFile file) {
        // Get the authenticated user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        try {
            // Call the service to handle the file upload and update the user record
            String photoUrl = profileService.updateProfilePhoto(email, file);
            return ResponseEntity.ok(photoUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update profile photo");
        }
    }

}
