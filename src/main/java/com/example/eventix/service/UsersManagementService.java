package com.example.eventix.service;


import com.example.eventix.dto.ReqRes;
import com.example.eventix.entity.Users;
import com.example.eventix.repository.UsersRepo;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.example.eventix.constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class UsersManagementService {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    private boolean isValidRegNoFormat(String regNo) {

        return regNo.matches("^(202\\d/[a-zA-Z]{2}/\\d{3})|(2021/[a-zA-Z]{2}/\\d{3})$");
    }

    // Helper method to validate email format
    private boolean isValidEmailFormat(String email) {
        return email.matches("^202\\d{1}(is|cs)\\d{3}@stu\\.ucsc\\.cmb\\.ac\\.lk$");
    }



    public ReqRes register(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();

        System.out.println("test pass");
        System.out.println(registrationRequest.getEmail());

        try {

//            if (!isValidRegNoFormat(registrationRequest.getRegNo())) {
//                resp.setStatusCode(400);
//                resp.setMessage("Invalid registration number format. It should be in the format 202X/is/000 or 2021/cs/000.");
//                return resp;
//            }

            // Validate email format
//            if (!isValidEmailFormat(registrationRequest.getEmail())) {
//                resp.setStatusCode(400);
//                resp.setMessage("Invalid email format. It should be in the format 202Xis000@stu.ucsc.cmb.ac.lk.");
//                return resp;
//            }


            Optional<Users> existingUserByRegNo = usersRepo.findByRegNo(registrationRequest.getRegNo());
            if (existingUserByRegNo.isPresent()) {
                resp.setStatusCode(400);
                resp.setMessage("Registration number already exists");
                return resp;
            }


            Optional<Users> existingUserByEmail = usersRepo.findByEmail(registrationRequest.getEmail());
            if (existingUserByEmail.isPresent()) {
                resp.setStatusCode(400);
                resp.setMessage("Email already registered");
                return resp;
            }
            System.out.println("test2");

            String otp = otpUtil.generateOtp();
            try {
                System.out.println("test otp");
                emailUtil.sendOtpEmail(registrationRequest.getEmail(), otp);
                System.out.println("test otp sent");
            } catch (MessagingException e) {
                throw new RuntimeException("Unable to send otp please try again");
            }

            System.out.println("test3");
            Users user = new Users();
            user.setEmail(registrationRequest.getEmail());
            user.setOtp(otp);
            user.setOtpGeneratedTime(LocalDateTime.now());
            user.setRegNo(registrationRequest.getRegNo());
            user.setFirstname(registrationRequest.getFirstname());
            user.setLastname(registrationRequest.getLastname());
            user.setRole(registrationRequest.getRole());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));


            /*if(file != null && !file.isEmpty()) {
                String fileName = "profile_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
                user.setImageData(FileUploadUtil.compressImage(file.getBytes()));
                //fileUploadUtil.saveFile(fileName, file.getBytes());
                user.setImageFileName(fileName);
                user.setImageContentType(file.getContentType());
            } /*else {
                byte[] defaultImageBytes = getDefaultImageBytes();
                String defaultFileName = "default_profile_image.png";
                fileUploadUtil.saveDefaultImage(defaultFileName, defaultImageBytes);
                user.setImageFileName(defaultFileName);
                user.setImageContentType("image/png");

            }

                byte[] defaultImageBytes = getDefaultImageBytes();
                String defaultFileName = "default_profile_image.png";
                fileUploadUtil.saveDefaultImage(defaultFileName, defaultImageBytes);
                user.setImageFileName(defaultFileName);
                user.setImageContentType("image/png");
                */

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

    private byte[] getDefaultImageBytes() throws IOException {
        InputStream is = getClass().getResourceAsStream("/static/images/default_profile_image.png");
        return IOUtils.toByteArray(is);
    }

    public ReqRes verifyAccount(String email, String otp) {
        ReqRes response = new ReqRes();

        try {
        Users user = usersRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));


            if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                    LocalDateTime.now()).getSeconds() < (60)) {
                user.setActive(true);
                usersRepo.save(user);
                response.setStatusCode(200);
                response.setMessage("OTP verified you can login");
            } else {
                response.setStatusCode(400);
                response.setMessage("Please regenerate otp and try again");
            }
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
            return response;
        }
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
            Users user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();

            if(!user.isActive()){
                response.setStatusCode(400);
                response.setMessage("Your account is not verified");
            }

            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
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

    public ReqRes regenerateOtp(String email) {
        ReqRes reqRes = new ReqRes();


        try {

            Users user = usersRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User Not found with this email: " + email));
            String otp = otpUtil.generateOtp();
            emailUtil.sendOtpEmail(email, otp);
            reqRes.setStatusCode(200);
            reqRes.setMessage("OTP generated successfully");
            user.setOtp(otp);
            user.setOtpGeneratedTime(LocalDateTime.now());
            usersRepo.save(user);
            reqRes.setMessage("Email sent... please verify account within 5 minutes");
            return reqRes;
        } catch (MessagingException e) {
            reqRes.setStatusCode(500);
            throw new RuntimeException("Unable to send otp please try again");
        }

    }

    public String uploadPhoto(Integer id, MultipartFile file) {
        log.info("Saving picture for userID: {}", id);
        Users user = usersRepo.findById(id).orElseThrow(()  -> new RuntimeException("User Not found with this id: " + id));
        String photoUrl = photoFunction.apply(id, file);
        user.setPhotoUrl(photoUrl);
        usersRepo.save(user);
        return photoUrl;
    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");

    private final BiFunction<Integer, MultipartFile, String> photoFunction = (id, image) -> {
        String filename = id + fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(image.getInputStream(),fileStorageLocation.resolve(filename), REPLACE_EXISTING);
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/static/image/" + id + fileExtension.apply(image.getOriginalFilename())).toUriString();
        } catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    };
}
