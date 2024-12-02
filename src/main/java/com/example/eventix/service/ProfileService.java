package com.example.eventix.service;

import com.example.eventix.dto.ProfileDTO;
import com.example.eventix.entity.Users;
import com.example.eventix.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProfileService {

    @Autowired
    private UsersRepo userRepository;  // This is the correct instance variable to use

    public ProfileDTO getProfileByEmail(String email) {
        Optional<Users> user = userRepository.findByEmail(email);  // Correct usage of instance variable
        if (user.isPresent()) {
            Users u = user.get();
            return new ProfileDTO(u.getFirstname(), u.getLastname(), u.getEmail(), u.getRegNo(), u.getPhotoUrl(), u.getBio());
        }
        return null;
    }

    public void updateBio(String email, String newBio) {
        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.setBio(newBio); // Make sure this is a plain string
            userRepository.save(user);
        }
    }


public String updateProfilePhoto(String email, MultipartFile file) throws IOException {
    Optional<Users> userOptional = userRepository.findByEmail(email);
    if (userOptional.isPresent()) {
        Users user = userOptional.get();

        // Define the path to save the uploaded file in the static directory
        String uploadDirectory = "src/main/resources/static/uploads/profile-photos/";
        String fileName = file.getOriginalFilename(); // Use the original file name
        Path filePath = Paths.get(uploadDirectory, fileName);

        // Ensure the directory exists
        Files.createDirectories(filePath.getParent());

        // Save the file to the defined path
        Files.write(filePath, file.getBytes());

        // Construct the URL to access the image
        String baseUrl = "https://eventix-spring-production.up.railway.app/uploads/profile-photos/";
        String photoUrl = baseUrl + fileName;

        // Store the URL in the database
        user.setPhotoUrl(photoUrl);

        userRepository.save(user);

        return photoUrl;
    } else {
        throw new RuntimeException("User with email " + email + " not found.");
    }
}


}
