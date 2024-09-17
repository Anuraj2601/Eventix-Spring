package com.example.eventix.service;

import com.example.eventix.dto.ProfileDTO;
import com.example.eventix.dto.UserProfileDTO;

import com.example.eventix.entity.Users;
import com.example.eventix.repository.UsersRepo;
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
}
