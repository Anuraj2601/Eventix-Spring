package com.example.eventix.service;

import com.example.eventix.entity.Users;
import com.example.eventix.repository.UsersRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JWTUtils {

    private SecretKey Key;
    private static final long EXPIRATION_TIME = 86400000;
    private final UsersRepo usersRepo;

    public JWTUtils(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
        String secretString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");

    }

    public String generateToken(UserDetails  userDetails) {
//        return Jwts.builder()
//                .subject(userDetails.getUsername())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(Key)
//                .compact();


        // Find the Users entity by email to get the user ID
        Optional<Users> optionalUser = usersRepo.findByEmail(userDetails.getUsername());
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("User not found with email: " + userDetails.getUsername());
        }
        Integer userId = optionalUser.get().getId();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("user_id", userId)  // Add user_id claim
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

     public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
//        return Jwts.builder()
//                .claims(claims)
//                .subject(userDetails.getUsername())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(Key)
//                .compact();

         Optional<Users> optionalUser = usersRepo.findByEmail(userDetails.getUsername());
         if (!optionalUser.isPresent()) {
             throw new IllegalArgumentException("User not found with email: " + userDetails.getUsername());
         }
         Integer userId = optionalUser.get().getId();

         claims.put("user_id", userId);  // Add user_id claim
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();

    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    public Integer extractUserId(String token) {
        return extractClaims(token, claims -> claims.get("user_id", Integer.class));
    }
}
