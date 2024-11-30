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
}
