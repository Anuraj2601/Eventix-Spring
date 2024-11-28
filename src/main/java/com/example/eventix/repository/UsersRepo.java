package com.example.eventix.repository;

import com.example.eventix.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByRegNo(String regNo);

    @Query("SELECT u.email FROM Users u WHERE u.id = :userId")
    String getEmailByUserId(int userId);
}
