package com.example.eventix.controller;


import com.example.eventix.dto.ReqRes;
import com.example.eventix.entity.Users;
import com.example.eventix.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.example.eventix.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@CrossOrigin(origins = "https://eventix-18.netlify.app")
public class UserManagementController {


    @Autowired
    private UsersManagementService usersManagementService;

    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes reg) {
        return ResponseEntity.ok(usersManagementService.register(reg));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes reg) {
        return ResponseEntity.ok(usersManagementService.login(reg));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes reg) {
        return ResponseEntity.ok(usersManagementService.refreshToken(reg));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqRes> getAllUsers() {
        return ResponseEntity.ok(usersManagementService.getAllUsers());
    }

    @GetMapping("/admin/get-users/{userid}")
    public ResponseEntity<ReqRes> getUserById(@PathVariable Integer userid) {
        return ResponseEntity.ok(usersManagementService.getUsersById(userid));
    }


    @PutMapping(value = "/admin/update/{userid}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable Integer userid, @RequestBody Users reqres) {
        return ResponseEntity.ok(usersManagementService.updateUser(userid, reqres));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = usersManagementService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete/{userid}")
    public ResponseEntity<ReqRes> deleteUser(@PathVariable Integer userid) {
        return ResponseEntity.ok(usersManagementService.deleteUser(userid));
    }

    @PutMapping("/verify-account")
    public ResponseEntity<ReqRes> verifyAccount(@RequestParam String email, @RequestParam String otp) {
        return ResponseEntity.ok(usersManagementService.verifyAccount(email, otp));
    }

    @PutMapping("/regenerate-otp")
    public ResponseEntity<ReqRes> regenerateOtp(@RequestParam String email) {
        return ResponseEntity.ok(usersManagementService.regenerateOtp(email));
    }

    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") Integer id ,@RequestParam("file")MultipartFile file) {
        return ResponseEntity.ok().body(usersManagementService.uploadPhoto(id, file));
    }

    @GetMapping(path = "static/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }
}
