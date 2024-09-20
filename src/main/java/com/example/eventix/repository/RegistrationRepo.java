package com.example.eventix.repository;

import com.example.eventix.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegistrationRepo extends JpaRepository<Registration, Integer> {

    @Modifying
    @Query("UPDATE Registration r SET r.position = :position WHERE r.email = :userId AND r.clubId = :clubId")
    void updatePositionByUserIdAndClubId(@Param("userId") String userId, @Param("position") String position, @Param("clubId") int clubId);
}
