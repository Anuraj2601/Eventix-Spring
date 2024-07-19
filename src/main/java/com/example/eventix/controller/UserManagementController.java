package com.example.eventix.controller;


import com.example.eventix.dto.ReqRes;
import com.example.eventix.entity.Users;
import com.example.eventix.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
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


    @PutMapping("/admin/update/{userid}")
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

    @PutMapping("/regenerate-otp"  )
    public ResponseEntity<ReqRes> regenerateOtp(@RequestParam String email) {
        return ResponseEntity.ok(usersManagementService.regenerateOtp(email));
    }


}
