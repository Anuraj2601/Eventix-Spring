package com.example.eventix.repository;

import com.example.eventix.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RegistrationRepo extends JpaRepository<Registration, Integer> {

    @Query("SELECT r FROM Registration r WHERE r.club.club_id = :clubId")
    List<Registration> findByClubId(@Param("clubId") int clubId);


}
