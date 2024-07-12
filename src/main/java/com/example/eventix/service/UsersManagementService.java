package com.example.eventix.service;


import com.example.eventix.dto.ReqRes;
import com.example.eventix.entity.Users;
import com.example.eventix.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UsersManagementService {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReqRes register(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();

        try {
            Users user = new Users();
            user.setEmail(registrationRequest.getEmail());
            user.setRegNo(registrationRequest.getRegNo());
            user.setFirstname(registrationRequest.getFirstname());
            user.setLastname(registrationRequest.getLastname());
            user.setRole(registrationRequest.getRole());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            Users usersResult = usersRepo.save(user);

            if(usersResult.getId() > 0) {
                resp.setUsers((usersResult));
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }


        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRegister) {
        ReqRes response = new ReqRes();

        try {
           String userEmail = jwtUtils.extractUsername(refreshTokenRegister.getToken());
           Users users = usersRepo.findByEmail(userEmail).orElseThrow();
           if(jwtUtils.isTokenValid(refreshTokenRegister.getToken(), users)) {
               var jwt = jwtUtils.generateToken(users);
               response.setStatusCode(200);
               response.setToken(jwt);
               response.setRefreshToken(refreshTokenRegister.getToken());
               response.setExpirationTime("24Hr");
               response.setMessage("Successfully Refreshed Token");
           }
            response.setStatusCode(200);
           return response;

        }catch(Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public ReqRes login(ReqRes loginRequest) {
        ReqRes response = new ReqRes();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");

        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();

        try {
            List<Users> result = usersRepo.findAll();
            if(!result.isEmpty()) {
                reqRes.setUsersList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        }catch(Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public ReqRes getUsersById(Integer id) {
        ReqRes reqRes = new ReqRes();

        try {
            Users usersById = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setUsers(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Users with id " + id + " found successfully");
        }catch(Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred" + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes deleteUser(Integer userId) {
        ReqRes reqRes = new ReqRes();

        try {
            Optional<Users> userOptional = usersRepo.findById(userId);
            if(userOptional.isPresent()) {
                usersRepo.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User Deleted Successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes updateUser(Integer userId, Users updatedUser) {
        ReqRes reqRes = new ReqRes();

        try {
            Optional<Users> userOptional = usersRepo.findById(userId);
            if(userOptional.isPresent()) {
                Users existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setRegNo(updatedUser.getRegNo());
                existingUser.setFirstname(updatedUser.getFirstname());

                existingUser.setLastname(updatedUser.getLastname());
                existingUser.setRole(updatedUser.getRole());

                if(updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                Users savedUser = usersRepo.save(existingUser);
                reqRes.setUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User Updated Successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        }catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getMyInfo(String email) {
        ReqRes reqRes = new ReqRes();

        try {
            Optional<Users> userOptional = usersRepo.findByEmail(email);
            if(userOptional.isPresent()) {
                reqRes.setUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        }catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;
    }
}
