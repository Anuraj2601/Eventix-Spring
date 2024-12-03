package com.example.eventix.service;

import com.example.eventix.dto.PostDTO;
import com.example.eventix.dto.ProfileDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.dto.UserProfileDTO;

import com.example.eventix.entity.Post;
import com.example.eventix.entity.Users;
import com.example.eventix.repository.UsersRepo;
import com.example.eventix.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private ResponseDTO responseDTO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepo.findByEmail(username).orElseThrow();
    }

    public List<UserProfileDTO> getAllUserProfiles() {
        List<Users> users = usersRepo.findAll();
        return users.stream()
                .map(user -> new UserProfileDTO(user.getFirstname() + " " + user.getLastname(), user.getPhotoUrl()))
                .collect(Collectors.toList());
    }
    public UserProfileDTO getUserProfileByEmail(String email) {
        Users user = usersRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserProfileDTO(user.getFirstname() + " " + user.getLastname(), user.getPhotoUrl());
    }

    public ProfileDTO getProfile(String email) {
        Users user = usersRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new ProfileDTO(user.getFirstname(), user.getLastname(), user.getEmail(), user.getRegNo(), user.getRole(), user.getBio(), user.getPhotoUrl() );
    }
    public List<Users> getAllUsers() {
        return usersRepo.findAll();
    }

    public ResponseDTO getUserByEmail(String email) {

        try{
            if(usersRepo.findByEmail(email).isPresent()){
                Users user = usersRepo.findByEmail(email).orElse(null);


                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("User Retrieved Successfully");
                responseDTO.setContent(user);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No User Found");
                responseDTO.setContent(null);


            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }

    }

    public ResponseDTO checkEmailActiveStatus(String email) {
        try {
            // Find the user by email (removes Optional and checks for null directly)
            Users user = usersRepo.findByEmail(email).orElse(null);

            if (user != null) {  // Check if the user is found
                // Check if the user is active (active == true)
                if (user.isActive()) {
                    // Return success if the user is active
                    responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                    responseDTO.setMessage("User is active.");
                    responseDTO.setContent(user);
                } else {
                    // Return a message if the user is inactive
                    responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                    responseDTO.setMessage("User is inactive. Please verify your email.");
                    responseDTO.setContent(null);
                }
            } else {
                // Return a message if no user is found with the given email
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No user found with this email.");
                responseDTO.setContent(null);
            }

            return responseDTO;

        } catch (Exception e) {
            // Handle any exceptions that might occur
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }
    }

    public ResponseDTO updateRoles(Integer currentAdminId, Integer currentTreasurerId, Integer newAdminId, Integer newTreasurerId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Users currentAdmin = usersRepo.findById(currentAdminId).orElseThrow(() -> new RuntimeException("Current admin not found"));
            Users currentTreasurer = usersRepo.findById(currentTreasurerId).orElseThrow(() -> new RuntimeException("Current treasurer not found"));
            Users newAdmin = usersRepo.findById(newAdminId).orElseThrow(() -> new RuntimeException("New admin not found"));
            Users newTreasurer = usersRepo.findById(newTreasurerId).orElseThrow(() -> new RuntimeException("New treasurer not found"));

            currentAdmin.setRole("student");
            currentTreasurer.setRole("student");
            newAdmin.setRole("ADMIN");
            newTreasurer.setRole("treasurer");

            usersRepo.save(currentAdmin);
            usersRepo.save(currentTreasurer);
            usersRepo.save(newAdmin);
            usersRepo.save(newTreasurer);

            responseDTO.setStatusCode(200);
            responseDTO.setMessage("Roles updated successfully.");
        } catch (Exception e) {
            responseDTO.setStatusCode(500);
            responseDTO.setMessage("Error: " + e.getMessage());
        }
        return responseDTO;
    }
}
