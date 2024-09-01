//package com.example.eventix.service;
//
//import com.example.eventix.dto.ProfileDTO;
//import com.example.eventix.entity.Users;
//import com.example.eventix.repository.UsersRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class ProfileService {
//
//    @Autowired
//    private UsersRepo userRepository;
//
//    public ProfileDTO getProfileByEmail(String email) {
//        Optional<Users> user = userRepository.findByEmail(email);
//        if (user.isPresent()) {
//            Users u = user.get();
//            return new ProfileDTO(u.getFirstname(), u.getLastname(), u.getEmail(), u.getRegNo(), u.getPhotoUrl(), u.getBio());
//        }
//        return null;
//    }
//
//    public void updateBio(String email, String newBio) {
//        Optional<Users> userOptional = UsersRepo.findByEmail(email);
//        if (userOptional.isPresent()) {
//            Users user = userOptional.get();
//            user.setBio(newBio);
//            UsersRepo.save(user); // Save the updated user
//        } else {
//            // Handle the case where the user is not found, maybe throw an exception or log a warning
//            System.out.println("User with email " + email + " not found.");
//        }
//    }
//
//}



package com.example.eventix.service;

import com.example.eventix.dto.ProfileDTO;
import com.example.eventix.entity.Users;
import com.example.eventix.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

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

//    public void updateBio(String email, String newBio) {
//        Optional<Users> userOptional = userRepository.findByEmail(email);  // Correct usage of instance variable
//        if (userOptional.isPresent()) {
//            Users user = userOptional.get();
//            user.setBio(newBio);
//            userRepository.save(user);  // Correct usage of instance variable
//        } else {
//            // Handle the case where the user is not found, maybe throw an exception or log a warning
//            System.out.println("User with email " + email + " not found.");
//        }
//    }

    // Method to update the bio
//    public void updateBio(String email, String newBio) {
//        Optional<Users> userOptional = userRepository.findByEmail(email);
//        if (userOptional.isPresent()) {
//            Users user = userOptional.get();
//            user.setBio(newBio);
//            userRepository.save(user); // Save the updated user
//        } else {
//            // Handle the case where the user is not found, maybe throw an exception or log a warning
//            System.out.println("User with email " + email + " not found.");
//        }
//    }

    public void updateBio(String email, String newBio) {
        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.setBio(newBio); // Make sure this is a plain string
            userRepository.save(user);
        }
    }

//    public void updateProfilePhoto(String email, MultipartFile file) throws IOException {
//        Optional<Users> userOptional = userRepository.findByEmail(email);
//        if (userOptional.isPresent()) {
//            Users user = userOptional.get();
//
//            // Convert the MultipartFile to a byte array
//            byte[] imageBytes = file.getBytes();
//
//            // Store the image bytes in the database
//            user.setPhotoUrl(imageBytes); // Assuming `photoUrl` is a byte[] in the `Users` entity
//
//            userRepository.save(user);
//        } else {
//            throw new RuntimeException("User with email " + email + " not found.");
//        }
//    }

}
